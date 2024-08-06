package com.leaveloper.authentication_domain.usecase

import com.leaveloper.authentication_domain.repository.AuthenticationRepository

class GetUserIdUseCase(private val repository: AuthenticationRepository) {
    operator fun invoke(): String? {
        return repository.getUserId()
    }
}