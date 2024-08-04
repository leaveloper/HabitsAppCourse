package com.leaveloper.habitsappcourse.home.data.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.leaveloper.habitsappcourse.home.data.local.HomeDao
import com.leaveloper.habitsappcourse.home.data.local.entity.HabitSyncEntity
import com.leaveloper.habitsappcourse.home.data.mapper.toDomain
import com.leaveloper.habitsappcourse.home.data.mapper.toDto
import com.leaveloper.habitsappcourse.home.data.remote.HomeApi
import com.leaveloper.habitsappcourse.home.data.remote.util.resultOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

@HiltWorker
class HabitSyncWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val workerParameters: WorkerParameters,
    private val api: HomeApi,
    private val dao: HomeDao
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 3) {
            return Result.failure()
        }

        val items = dao.getAllHabitsSync()

        return try {
            // Si uno de los llamados falla llama a
            // Result.retry
            // Si se llama mas de 3 veces, deja de intentar

            supervisorScope {
                val jobs = items.map { item ->
                    launch {
                        sync(item)
                    }
                }

                jobs.forEach { it.join() }
            }

            Result.success()

        } catch (e: Exception) {
            Result.retry()
        }

        /*items.forEach {
            sync(it.id)
        }*/
    }

    private suspend fun sync(entity: HabitSyncEntity) {
        val habit = dao.getHabitById(entity.id).toDomain().toDto()
        resultOf {
            api.insertHabit(habit)
        }.onSuccess {
            dao.deleteHabitSync(entity)
        }.onFailure {
            throw it
        }
    }
}