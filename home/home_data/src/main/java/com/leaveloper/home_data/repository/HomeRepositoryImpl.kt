package com.leaveloper.home_data.repository

import android.os.Build
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.leaveloper.home_data.extension.toStartOfDateTimestamp
import com.leaveloper.home_data.local.HomeDao
import com.leaveloper.home_data.local.entity.HabitSyncEntity
import com.leaveloper.home_data.mapper.toDomain
import com.leaveloper.home_data.mapper.toDto
import com.leaveloper.home_data.mapper.toEntity
import com.leaveloper.home_data.mapper.toSyncEntity
import com.leaveloper.home_data.remote.HomeApi
import com.leaveloper.home_data.remote.util.resultOf
import com.leaveloper.home_data.sync.HabitSyncWorker
import com.leaveloper.home_domain.alarm.AlarmHandler
import com.leaveloper.home_domain.models.Habit
import com.leaveloper.home_domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.time.Duration
import java.time.ZonedDateTime

class HomeRepositoryImpl(
    private val dao: HomeDao,
    private val api: HomeApi,
    private val alarmHandler: com.leaveloper.home_domain.alarm.AlarmHandler,
    private val workManager: WorkManager
) : com.leaveloper.home_domain.repository.HomeRepository {
    override fun getAllHabitsForSelectedDate(date: ZonedDateTime): Flow<List<com.leaveloper.home_domain.models.Habit>> {
        val localFlow = dao.getAllHabitsForSelectedDate(date.toStartOfDateTimestamp()).map { it ->
            it.map {
                it.toDomain()
            }
        }

        val apiFlow = getHabitsFromApi()

        return localFlow.combine(apiFlow) { db, _ ->
            db
        }
    }

    private fun getHabitsFromApi(): Flow<List<com.leaveloper.home_domain.models.Habit>> {
        return flow {
            resultOf {
                val habits = api.getAllHabits()
                insertHabits(habits.toDomain())
            }

            emit(emptyList<com.leaveloper.home_domain.models.Habit>())
        }.onStart {
            // Se salta la primera emisión para evitar esperar
            emit(emptyList())
        }
    }

    override suspend fun insertHabit(habit: com.leaveloper.home_domain.models.Habit) {
        handleAlarm(habit)
        dao.insertHabit(habit.toEntity())
        resultOf {
            api.insertHabit(habit.toDto())
        }.onFailure {
            dao.insertHabitSync(habit.toSyncEntity())
        }
    }

    private suspend fun insertHabits(habits: List<com.leaveloper.home_domain.models.Habit>) {
        habits.forEach {
            handleAlarm(it)
            dao.insertHabit(it.toEntity())
        }
    }

    private suspend fun handleAlarm(habit: com.leaveloper.home_domain.models.Habit) {
        try {
            val previous = dao.getHabitById(habit.id)
            alarmHandler.cancel(previous.toDomain())
        } catch (e: Exception) { /* Habit doesn't exist */
        }

        alarmHandler.setRecurringAlarm(habit)
    }

    override suspend fun getHabitById(id: String): com.leaveloper.home_domain.models.Habit {
        return dao.getHabitById(id).toDomain()
    }

    override suspend fun syncHabits() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return

        val worker = OneTimeWorkRequestBuilder<HabitSyncWorker>()
                .setConstraints(
                Constraints
                    .Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
                )
                // Si falla luego de 3 intentos, va a esperar 5 minutos antes de volver a intentar
                // Este tiempo es exponencial, por lo tanto, cada intervalo de reintento va a ser más largo
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, Duration.ofMinutes(5))
                .build()

        // Si el usuario cerro la aplicación y la volvió a abrir
        // REPLACE reinicia el intervalo de reintento a 0 y la aplicación intenta inmediatamente
        workManager.beginUniqueWork("sync_habit_id", ExistingWorkPolicy.REPLACE, worker).enqueue()
    }
}