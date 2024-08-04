package com.leaveloper.habitsappcourse.home.domain.detail.usecase

import com.leaveloper.habitsappcourse.home.domain.models.Habit
import com.leaveloper.habitsappcourse.home.domain.repository.HomeRepository

class GetHabitByIdUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(id: String): Habit {
        return repository.getHabitById(id)
    }
}