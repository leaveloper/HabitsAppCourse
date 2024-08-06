package com.leaveloper.home_domain.home.usecase

data class HomeUseCases(
    val completeHabitUseCase: CompleteHabitUseCase,
    val getAllHabitsForDateUseCase: GetAllHabitsForDateUseCase,
    val syncHabitUseCase: SyncHabitUseCase
)