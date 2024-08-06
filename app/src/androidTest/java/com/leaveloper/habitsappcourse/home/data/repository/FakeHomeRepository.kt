package com.leaveloper.habitsappcourse.home.data.repository

import com.leaveloper.habitsappcourse.home.domain.models.Habit
import com.leaveloper.habitsappcourse.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.time.ZonedDateTime

class FakeHomeRepository : HomeRepository {
    private var habits = emptyList<Habit>()
    private val habitsFlow = MutableSharedFlow<List<Habit>>()

    override fun getAllHabitsForSelectedDate(date: ZonedDateTime) = habitsFlow

    override suspend fun insertHabit(habit: Habit) {
        habits = habits + habit
        habitsFlow.emit(habits)
    }

    override suspend fun getHabitById(id: String) = habits.first { id == it.id }

    override suspend fun syncHabits() { }
}