package com.leaveloper.habitsappcourse.home.data.repository

import com.leaveloper.home_domain.models.Habit
import com.leaveloper.home_domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.time.ZonedDateTime

class FakeHomeRepository : com.leaveloper.home_domain.repository.HomeRepository {
    private var habits = emptyList<com.leaveloper.home_domain.models.Habit>()
    private val habitsFlow = MutableSharedFlow<List<com.leaveloper.home_domain.models.Habit>>()

    override fun getAllHabitsForSelectedDate(date: ZonedDateTime) = habitsFlow

    override suspend fun insertHabit(habit: com.leaveloper.home_domain.models.Habit) {
        habits = habits + habit
        habitsFlow.emit(habits)
    }

    override suspend fun getHabitById(id: String) = habits.first { id == it.id }

    override suspend fun syncHabits() { }
}