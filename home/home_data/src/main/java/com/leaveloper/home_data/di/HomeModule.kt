package com.leaveloper.home_data.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.leaveloper.home_data.alarm.AlarmHandlerImpl
import com.leaveloper.home_data.local.HomeDao
import com.leaveloper.home_data.local.HomeDatabase
import com.leaveloper.home_data.local.typeconverter.HomeTypeConverter
import com.leaveloper.home_data.remote.HomeApi
import com.leaveloper.home_data.repository.HomeRepositoryImpl
import com.leaveloper.home_domain.alarm.AlarmHandler
import com.leaveloper.home_domain.detail.usecase.DetailUseCases
import com.leaveloper.home_domain.detail.usecase.GetHabitByIdUseCase
import com.leaveloper.home_domain.detail.usecase.InsertHabitUseCase
import com.leaveloper.home_domain.home.usecase.CompleteHabitUseCase
import com.leaveloper.home_domain.home.usecase.GetAllHabitsForDateUseCase
import com.leaveloper.home_domain.home.usecase.HomeUseCases
import com.leaveloper.home_domain.home.usecase.SyncHabitUseCase
import com.leaveloper.home_domain.repository.HomeRepository
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
    fun provideHomeUseCases(repository: com.leaveloper.home_domain.repository.HomeRepository): com.leaveloper.home_domain.home.usecase.HomeUseCases {
        return com.leaveloper.home_domain.home.usecase.HomeUseCases(
            getAllHabitsForDateUseCase = com.leaveloper.home_domain.home.usecase.GetAllHabitsForDateUseCase(
                repository
            ),
            completeHabitUseCase = com.leaveloper.home_domain.home.usecase.CompleteHabitUseCase(
                repository
            ),
            syncHabitUseCase = com.leaveloper.home_domain.home.usecase.SyncHabitUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideDetailUseCases(repository: com.leaveloper.home_domain.repository.HomeRepository): com.leaveloper.home_domain.detail.usecase.DetailUseCases {
        return com.leaveloper.home_domain.detail.usecase.DetailUseCases(
            getHabitByIdUseCase = com.leaveloper.home_domain.detail.usecase.GetHabitByIdUseCase(
                repository
            ),
            insertHabitUseCase = com.leaveloper.home_domain.detail.usecase.InsertHabitUseCase(
                repository
            )
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
    fun provideAlarmHandler(@ApplicationContext context: Context): com.leaveloper.home_domain.alarm.AlarmHandler {
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
    fun provideHomeRepository(dao: HomeDao, api: HomeApi, alarmHandler: com.leaveloper.home_domain.alarm.AlarmHandler, workManager: WorkManager): com.leaveloper.home_domain.repository.HomeRepository {
        return HomeRepositoryImpl(dao, api, alarmHandler, workManager)
    }
}