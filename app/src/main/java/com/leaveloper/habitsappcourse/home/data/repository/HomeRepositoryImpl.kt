package com.leaveloper.habitsappcourse.home.data.repository

import android.util.Log
import com.leaveloper.habitsappcourse.home.data.extension.toStartOfDateTimestamp
import com.leaveloper.habitsappcourse.home.data.local.HomeDao
import com.leaveloper.habitsappcourse.home.data.mapper.toDomain
import com.leaveloper.habitsappcourse.home.data.mapper.toDto
import com.leaveloper.habitsappcourse.home.data.mapper.toEntity
import com.leaveloper.habitsappcourse.home.data.remote.HomeApi
import com.leaveloper.habitsappcourse.home.data.remote.util.resultOf
import com.leaveloper.habitsappcourse.home.domain.models.Habit
import com.leaveloper.habitsappcourse.home.domain.repository.HomeRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.time.ZonedDateTime

class HomeRepositoryImpl(
    private val dao: HomeDao,
    private val api: HomeApi
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
        dao.insertHabit(habit.toEntity())
        resultOf {
            api.insertHabit(habit.toDto())
        }
    }

    private suspend fun insertHabits(habits: List<Habit>) {
        dao.insertHabits(habits.map { it.toEntity() })
    }

    override suspend fun getHabitById(id: String): Habit {
        return dao.getHabitById(id).toDomain()
    }
}