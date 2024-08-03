package com.leaveloper.habitsappcourse.home.presentation.home.di

import com.leaveloper.habitsappcourse.home.presentation.home.data.repository.HomeRepositoryImpl
import com.leaveloper.habitsappcourse.home.presentation.home.domain.home.usecase.CompleteHabitUseCase
import com.leaveloper.habitsappcourse.home.presentation.home.domain.home.usecase.GetAllHabitsForDateUseCase
import com.leaveloper.habitsappcourse.home.presentation.home.domain.home.usecase.HomeUseCases
import com.leaveloper.habitsappcourse.home.presentation.home.domain.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {
    @Provides
    @Singleton
    fun provideHomeUseCases(repository: HomeRepository) : HomeUseCases {
        return HomeUseCases(
            getAllHabitsForDateUseCase = GetAllHabitsForDateUseCase(repository),
            completeHabitUseCase = CompleteHabitUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideHomeRepository() : HomeRepository {
        return HomeRepositoryImpl()
    }
}