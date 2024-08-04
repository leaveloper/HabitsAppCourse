package com.leaveloper.habitsappcourse.home.data.repository

import android.os.Build
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.leaveloper.habitsappcourse.home.data.extension.toStartOfDateTimestamp
import com.leaveloper.habitsappcourse.home.data.local.HomeDao
import com.leaveloper.habitsappcourse.home.data.local.entity.HabitSyncEntity
import com.leaveloper.habitsappcourse.home.data.mapper.toDomain
import com.leaveloper.habitsappcourse.home.data.mapper.toDto
import com.leaveloper.habitsappcourse.home.data.mapper.toEntity
import com.leaveloper.habitsappcourse.home.data.mapper.toSyncEntity
import com.leaveloper.habitsappcourse.home.data.remote.HomeApi
import com.leaveloper.habitsappcourse.home.data.remote.util.resultOf
import com.leaveloper.habitsappcourse.home.data.sync.HabitSyncWorker
import com.leaveloper.habitsappcourse.home.domain.alarm.AlarmHandler
import com.leaveloper.habitsappcourse.home.domain.models.Habit
import com.leaveloper.habitsappcourse.home.domain.repository.HomeRepository
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
    private val alarmHandler: AlarmHandler,
    private val workManager: WorkManager
) : HomeRepository {
    override fun getAllHabitsForSelectedDate(date: ZonedDateTime): Flow<List<Habit>> {
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

    private fun getHabitsFromApi(): Flow<List<Habit>> {
        return flow {
            resultOf {
                val habits = api.getAllHabits()
                insertHabits(habits.toDomain())
            }

            emit(emptyList<Habit>())
        }.onStart {
            // Se salta la primera emisión para evitar esperar
            emit(emptyList())
        }
    }

    override suspend fun insertHabit(habit: Habit) {
        handleAlarm(habit)
        dao.insertHabit(habit.toEntity())
        resultOf {
            api.insertHabit(habit.toDto())
        }.onFailure {
            dao.insertHabitSync(habit.toSyncEntity())
        }
    }

    private suspend fun insertHabits(habits: List<Habit>) {
        habits.forEach {
            handleAlarm(it)
            dao.insertHabit(it.toEntity())
        }
    }

    private suspend fun handleAlarm(habit: Habit) {
        try {
            val previous = dao.getHabitById(habit.id)
            alarmHandler.cancel(previous.toDomain())
        } catch (e: Exception) { /* Habit doesn't exist */
        }

        alarmHandler.setRecurringAlarm(habit)
    }

    override suspend fun getHabitById(id: String): Habit {
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