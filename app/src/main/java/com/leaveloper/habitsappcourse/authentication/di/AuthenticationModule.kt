package com.leaveloper.habitsappcourse.authentication.di

import com.leaveloper.habitsappcourse.authentication.data.matcher.EmailMatcherImpl
import com.leaveloper.habitsappcourse.authentication.data.repository.AuthenticationRepositoryImpl
import com.leaveloper.habitsappcourse.authentication.domain.matcher.EmailMatcher
import com.leaveloper.habitsappcourse.authentication.domain.repository.AuthenticationRepository
import com.leaveloper.habitsappcourse.authentication.domain.usecase.GetUserIdUseCase
import com.leaveloper.habitsappcourse.authentication.domain.usecase.LoginUseCases
import com.leaveloper.habitsappcourse.authentication.domain.usecase.LoginWithEmailUseCase
import com.leaveloper.habitsappcourse.authentication.domain.usecase.SignupUseCases
import com.leaveloper.habitsappcourse.authentication.domain.usecase.SignupWithEmailUseCase
import com.leaveloper.habitsappcourse.authentication.domain.usecase.ValidateEmailUseCase
import com.leaveloper.habitsappcourse.authentication.domain.usecase.ValidatePasswordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {
    @Provides
    @Singleton
    fun provideAuthenticationRepository(): AuthenticationRepository {
        return AuthenticationRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideEmailMatcher(): EmailMatcher {
        return EmailMatcherImpl()
    }

    @Provides
    @Singleton
    fun provideLoginUseCases(repository: AuthenticationRepository, emailMatcher: EmailMatcher): LoginUseCases {
        return LoginUseCases(
            loginWithEmailUseCase = LoginWithEmailUseCase(repository),
            validateEmailUseCase = ValidateEmailUseCase(emailMatcher),
            validatePasswordUseCase = ValidatePasswordUseCase()
        )
    }

    @Provides
    @Singleton
    fun provideSignupUseCases(repository: AuthenticationRepository, emailMatcher: EmailMatcher): SignupUseCases {
        return SignupUseCases(
            signupWithEmailUseCase = SignupWithEmailUseCase(repository),
            validateEmailUseCase = ValidateEmailUseCase(emailMatcher),
            validatePasswordUseCase = ValidatePasswordUseCase()
        )
    }

    @Provides
    @Singleton
    fun provideGetUserIdUseCase(repository: AuthenticationRepository) : GetUserIdUseCase {
        return GetUserIdUseCase(repository)
    }
}