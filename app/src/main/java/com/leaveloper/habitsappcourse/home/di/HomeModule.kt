package com.leaveloper.habitsappcourse.home.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.leaveloper.habitsappcourse.home.data.alarm.AlarmHandlerImpl
import com.leaveloper.habitsappcourse.home.data.local.HomeDao
import com.leaveloper.habitsappcourse.home.data.local.HomeDatabase
import com.leaveloper.habitsappcourse.home.data.local.typeconverter.HomeTypeConverter
import com.leaveloper.habitsappcourse.home.data.remote.HomeApi
import com.leaveloper.habitsappcourse.home.data.repository.HomeRepositoryImpl
import com.leaveloper.habitsappcourse.home.domain.alarm.AlarmHandler
import com.leaveloper.habitsappcourse.home.domain.detail.usecase.DetailUseCases
import com.leaveloper.habitsappcourse.home.domain.detail.usecase.GetHabitByIdUseCase
import com.leaveloper.habitsappcourse.home.domain.detail.usecase.InsertHabitUseCase
import com.leaveloper.habitsappcourse.home.domain.home.usecase.CompleteHabitUseCase
import com.leaveloper.habitsappcourse.home.domain.home.usecase.GetAllHabitsForDateUseCase
import com.leaveloper.habitsappcourse.home.domain.home.usecase.HomeUseCases
import com.leaveloper.habitsappcourse.home.domain.home.usecase.SyncHabitUseCase
import com.leaveloper.habitsappcourse.home.domain.repository.HomeRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {
    @Provides
    @Singleton
    fun provideHomeUseCases(repository: HomeRepository): HomeUseCases {
        return HomeUseCases(
            getAllHabitsForDateUseCase = GetAllHabitsForDateUseCase(repository),
            completeHabitUseCase = CompleteHabitUseCase(repository),
            syncHabitUseCase = SyncHabitUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideDetailUseCases(repository: HomeRepository): DetailUseCases {
        return DetailUseCases(
            getHabitByIdUseCase = GetHabitByIdUseCase(repository),
            insertHabitUseCase = InsertHabitUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideHabitDao(@ApplicationContext context: Context): HomeDao {
        return Room.databaseBuilder(
            context,
            HomeDatabase::class.java,
            "habits_db"
        ).addTypeConverter(HomeTypeConverter()).build().dao
    }

    @Provides
    @Singleton
    fun provideAlarmHandler(@ApplicationContext context: Context): AlarmHandler {
        return AlarmHandlerImpl(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        // Permite ver los logs en Logcat
        return OkHttpClient
            .Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
            ).build()
    }

    @Provides
    @Singleton
    fun provideHomeApi(client: OkHttpClient): HomeApi {
        return Retrofit
            .Builder()
            .baseUrl(HomeApi.BASE_URL)
            .client(client)
            .addConverterFactory(
                MoshiConverterFactory.create()
            )
            .build()
            .create(HomeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context) : WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(dao: HomeDao, api: HomeApi, alarmHandler: AlarmHandler, workManager: WorkManager): HomeRepository {
        return HomeRepositoryImpl(dao, api, alarmHandler, workManager)
    }
}