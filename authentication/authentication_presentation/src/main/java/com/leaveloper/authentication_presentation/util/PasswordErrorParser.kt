package com.leaveloper.authentication_presentation.util

import com.leaveloper.authentication_domain.usecase.PasswordResult

object PasswordErrorParser {
    fun parseError(error: com.leaveloper.authentication_domain.usecase.PasswordResult) : String? {
        return when (error) {
            com.leaveloper.authentication_domain.usecase.PasswordResult.VALID -> null
            com.leaveloper.authentication_domain.usecase.PasswordResult.INVALID_LOWERCASE -> "La contraseña debe tener al menos 1 caracter en minúscula"
            com.leaveloper.authentication_domain.usecase.PasswordResult.INVALID_UPPERCASE -> "La contraseña debe tener al menos 1 caracter en mayúscula"
            com.leaveloper.authentication_domain.usecase.PasswordResult.INVALID_DIGITS -> "La contraseña debe tener al menos 1 número"
            com.leaveloper.authentication_domain.usecase.PasswordResult.INVALID_LENGTH -> "La contraseña debe tener al menos 8 caracteres"
        }
    }
}