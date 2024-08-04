package com.leaveloper.habitsappcourse.authentication.domain.usecase

import com.leaveloper.habitsappcourse.authentication.domain.repository.AuthenticationRepository

class LogoutUseCase(private val repository: AuthenticationRepository) {
    suspend operator fun invoke() {
        return repository.logout()
    }
}