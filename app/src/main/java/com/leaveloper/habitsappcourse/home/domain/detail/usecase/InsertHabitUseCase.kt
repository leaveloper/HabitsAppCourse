package com.leaveloper.habitsappcourse.home.domain.detail.usecase

import com.leaveloper.habitsappcourse.home.domain.models.Habit
import com.leaveloper.habitsappcourse.home.domain.repository.HomeRepository
import java.time.ZonedDateTime

class InsertHabitUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(habit: Habit) {
        repository.insertHabit(habit)
    }
}