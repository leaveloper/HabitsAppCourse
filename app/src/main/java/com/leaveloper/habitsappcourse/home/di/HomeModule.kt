package com.leaveloper.habitsappcourse.home.di

import android.content.Context
import androidx.room.Room
import com.leaveloper.habitsappcourse.home.data.local.HomeDao
import com.leaveloper.habitsappcourse.home.data.local.HomeDatabase
import com.leaveloper.habitsappcourse.home.data.local.typeconverter.HomeTypeConverter
import com.leaveloper.habitsappcourse.home.data.repository.HomeRepositoryImpl
import com.leaveloper.habitsappcourse.home.domain.detail.usecase.DetailUseCases
import com.leaveloper.habitsappcourse.home.domain.detail.usecase.GetHabitByIdUseCase
import com.leaveloper.habitsappcourse.home.domain.detail.usecase.InsertHabitUseCase
import com.leaveloper.habitsappcourse.home.domain.home.usecase.CompleteHabitUseCase
import com.leaveloper.habitsappcourse.home.domain.home.usecase.GetAllHabitsForDateUseCase
import com.leaveloper.habitsappcourse.home.domain.home.usecase.HomeUseCases
import com.leaveloper.habitsappcourse.home.domain.repository.HomeRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideHabitDao(@ApplicationContext context: Context, moshi: Moshi): HomeDao {
        return Room.databaseBuilder(
            context,
            HomeDatabase::class.java,
            "habits_db"
        ).addTypeConverter(HomeTypeConverter(moshi)).build().dao
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    @Singleton
    fun provideHomeRepository(dao: HomeDao) : HomeRepository {
        return HomeRepositoryImpl(dao)
    }
}