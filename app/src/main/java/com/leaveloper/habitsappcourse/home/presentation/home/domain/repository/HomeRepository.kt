package com.leaveloper.habitsappcourse.home.presentation.home.domain.repository

import com.leaveloper.habitsappcourse.home.presentation.home.domain.models.Habit
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

interface HomeRepository {
    fun getAllHabitsForSelectedDate(date: ZonedDateTime): Flow<List<Habit>>
    suspend fun insertHabit(habit: Habit)
}