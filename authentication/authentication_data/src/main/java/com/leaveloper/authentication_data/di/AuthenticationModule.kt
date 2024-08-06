package com.leaveloper.authentication_data.di

import com.leaveloper.authentication_data.matcher.EmailMatcherImpl
import com.leaveloper.authentication_data.repository.AuthenticationRepositoryImpl
import com.leaveloper.authentication_domain.matcher.EmailMatcher
import com.leaveloper.authentication_domain.repository.AuthenticationRepository
import com.leaveloper.authentication_domain.usecase.GetUserIdUseCase
import com.leaveloper.authentication_domain.usecase.LoginUseCases
import com.leaveloper.authentication_domain.usecase.LoginWithEmailUseCase
import com.leaveloper.authentication_domain.usecase.LogoutUseCase
import com.leaveloper.authentication_domain.usecase.SignupUseCases
import com.leaveloper.authentication_domain.usecase.SignupWithEmailUseCase
import com.leaveloper.authentication_domain.usecase.ValidateEmailUseCase
import com.leaveloper.authentication_domain.usecase.ValidatePasswordUseCase
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
    fun provideAuthenticationRepository(): com.leaveloper.authentication_domain.repository.AuthenticationRepository {
        return AuthenticationRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideEmailMatcher(): com.leaveloper.authentication_domain.matcher.EmailMatcher {
        return EmailMatcherImpl()
    }

    @Provides
    @Singleton
    fun provideLoginUseCases(repository: com.leaveloper.authentication_domain.repository.AuthenticationRepository, emailMatcher: com.leaveloper.authentication_domain.matcher.EmailMatcher): com.leaveloper.authentication_domain.usecase.LoginUseCases {
        return com.leaveloper.authentication_domain.usecase.LoginUseCases(
            loginWithEmailUseCase = com.leaveloper.authentication_domain.usecase.LoginWithEmailUseCase(
                repository
            ),
            validateEmailUseCase = com.leaveloper.authentication_domain.usecase.ValidateEmailUseCase(
                emailMatcher
            ),
            validatePasswordUseCase = com.leaveloper.authentication_domain.usecase.ValidatePasswordUseCase()
        )
    }

    @Provides
    @Singleton
    fun provideSignupUseCases(repository: com.leaveloper.authentication_domain.repository.AuthenticationRepository, emailMatcher: com.leaveloper.authentication_domain.matcher.EmailMatcher): com.leaveloper.authentication_domain.usecase.SignupUseCases {
        return com.leaveloper.authentication_domain.usecase.SignupUseCases(
            signupWithEmailUseCase = com.leaveloper.authentication_domain.usecase.SignupWithEmailUseCase(
                repository
            ),
            validateEmailUseCase = com.leaveloper.authentication_domain.usecase.ValidateEmailUseCase(
                emailMatcher
            ),
            validatePasswordUseCase = com.leaveloper.authentication_domain.usecase.ValidatePasswordUseCase()
        )
    }

    @Provides
    @Singleton
    fun provideGetUserIdUseCase(repository: com.leaveloper.authentication_domain.repository.AuthenticationRepository) : com.leaveloper.authentication_domain.usecase.GetUserIdUseCase {
        return com.leaveloper.authentication_domain.usecase.GetUserIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(repository: com.leaveloper.authentication_domain.repository.AuthenticationRepository) : com.leaveloper.authentication_domain.usecase.LogoutUseCase {
        return com.leaveloper.authentication_domain.usecase.LogoutUseCase(repository)
    }
}