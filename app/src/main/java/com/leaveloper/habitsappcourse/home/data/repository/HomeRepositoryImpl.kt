package com.leaveloper.habitsappcourse.home.data.repository

import com.leaveloper.habitsappcourse.home.data.extension.toStartOfDateTimestamp
import com.leaveloper.habitsappcourse.home.data.local.HomeDao
import com.leaveloper.habitsappcourse.home.data.mapper.toDomain
import com.leaveloper.habitsappcourse.home.data.mapper.toEntity
import com.leaveloper.habitsappcourse.home.domain.models.Habit
import com.leaveloper.habitsappcourse.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.ZonedDateTime

class HomeRepositoryImpl(
    private val dao: HomeDao
) : HomeRepository {
    override fun getAllHabitsForSelectedDate(date: ZonedDateTime): Flow<List<Habit>> {
        return dao.getAllHabitsForSelectedDate(date.toStartOfDateTimestamp()).map {
            it.map {
                it.toDomain()
            }
        }
    }

    override suspend fun insertHabit(habit: Habit) {
        dao.insertHabit(habit.toEntity())
    }

    override suspend fun getHabitById(id: String): Habit {
        return dao.getHabitById(id).toDomain()
    }
}