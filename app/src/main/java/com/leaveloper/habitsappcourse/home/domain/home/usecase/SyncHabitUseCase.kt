package com.leaveloper.habitsappcourse.home.domain.home.usecase

import com.leaveloper.habitsappcourse.home.domain.repository.HomeRepository

class SyncHabitUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke() {
        repository.syncHabits()
    }
}