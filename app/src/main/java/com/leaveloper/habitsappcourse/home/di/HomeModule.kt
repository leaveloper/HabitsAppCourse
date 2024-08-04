package com.leaveloper.habitsappcourse.home.di

import com.leaveloper.habitsappcourse.home.data.repository.HomeRepositoryImpl
import com.leaveloper.habitsappcourse.home.domain.detail.usecase.DetailUseCases
import com.leaveloper.habitsappcourse.home.domain.detail.usecase.GetHabitByIdUseCase
import com.leaveloper.habitsappcourse.home.domain.detail.usecase.InsertHabitUseCase
import com.leaveloper.habitsappcourse.home.domain.home.usecase.CompleteHabitUseCase
import com.leaveloper.habitsappcourse.home.domain.home.usecase.GetAllHabitsForDateUseCase
import com.leaveloper.habitsappcourse.home.domain.home.usecase.HomeUseCases
import com.leaveloper.habitsappcourse.home.domain.repository.HomeRepository
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
    fun provideDetailUseCases(repository: HomeRepository) : DetailUseCases {
        return DetailUseCases(
            getHabitByIdUseCase = GetHabitByIdUseCase(repository),
            insertHabitUseCase = InsertHabitUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideHomeRepository() : HomeRepository {
        return HomeRepositoryImpl()
    }
}