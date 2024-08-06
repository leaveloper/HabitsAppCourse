package com.leaveloper.onboarding_data.di

import android.content.Context
import android.content.SharedPreferences
import com.leaveloper.onboarding_data.OnboardingRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object OnboardingModule {
    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("habits_onboarding_preferences", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideOnboardingRepository(sharedPreferences: SharedPreferences): com.leaveloper.onboarding_domain.repository.OnboardingRepository {
        return OnboardingRepositoryImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideHasSeenOnboardingUseCase(repository: com.leaveloper.onboarding_domain.repository.OnboardingRepository) : com.leaveloper.onboarding_domain.usecases.HasSeenOnboardingUseCase {
        return com.leaveloper.onboarding_domain.usecases.HasSeenOnboardingUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCompleteOnboardingUseCase(repository: com.leaveloper.onboarding_domain.repository.OnboardingRepository) : com.leaveloper.onboarding_domain.usecases.CompleteOnboardingUseCase {
        return com.leaveloper.onboarding_domain.usecases.CompleteOnboardingUseCase(repository)
    }
}