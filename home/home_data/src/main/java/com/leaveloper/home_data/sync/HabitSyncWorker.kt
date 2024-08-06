package com.leaveloper.home_data.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.leaveloper.home_data.local.HomeDao
import com.leaveloper.home_data.local.entity.HabitSyncEntity
import com.leaveloper.home_data.mapper.toDomain
import com.leaveloper.home_data.mapper.toDto
import com.leaveloper.home_data.remote.HomeApi
import com.leaveloper.home_data.remote.util.resultOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.joinAll
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
                    async {
                        sync(item)
                    }

                    /*
                    Si se utiliza launch y lanza una excepción,
                    esta se propaga y crashea toda la aplicación

                    launch {
                        sync(item)
                    }
                    */
                }

                /*
                Si se utiliza launch, se puede utilizar
                jobs.awaitAll()
                */
                jobs.awaitAll()
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