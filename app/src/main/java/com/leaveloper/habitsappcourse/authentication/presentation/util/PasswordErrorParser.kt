package com.leaveloper.habitsappcourse.authentication.presentation.util

import com.leaveloper.habitsappcourse.authentication.domain.usecase.PasswordResult

object PasswordErrorParser {
    fun parseError(error: PasswordResult) : String? {
        return when (error) {
            PasswordResult.VALID -> null
            PasswordResult.INVALID_LOWERCASE -> "La contraseña debe tener al menos 1 caracter en minúscula"
            PasswordResult.INVALID_UPPERCASE -> "La contraseña debe tener al menos 1 caracter en mayúscula"
            PasswordResult.INVALID_DIGITS -> "La contraseña debe tener al menos 1 número"
            PasswordResult.INVALID_LENGTH -> "La contraseña debe tener al menos 8 caracteres"
        }
    }
}