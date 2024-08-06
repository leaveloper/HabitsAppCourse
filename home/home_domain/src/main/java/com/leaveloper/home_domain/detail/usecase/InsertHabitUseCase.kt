package com.leaveloper.home_domain.detail.usecase

import com.leaveloper.home_domain.models.Habit
import com.leaveloper.home_domain.repository.HomeRepository
import java.time.ZonedDateTime

class InsertHabitUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(habit: Habit) {
        repository.insertHabit(habit)
    }
}