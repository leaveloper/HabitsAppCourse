package com.leaveloper.habitsappcourse.authentication.domain.usecase

import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class ValidatePasswordUseCaseTest {
    private lateinit var validatePasswordUseCase: com.leaveloper.authentication_domain.usecase.ValidatePasswordUseCase

    @Before
    fun setup() {
        validatePasswordUseCase =
            com.leaveloper.authentication_domain.usecase.ValidatePasswordUseCase()
    }

    @Test
    fun `given low character password, return invalid password`() {
        val input = "asd"
        val result = validatePasswordUseCase(input)

        assertEquals(
            com.leaveloper.authentication_domain.usecase.PasswordResult.INVALID_LENGTH,
            result
        )
    }

    @Test
    fun `given no lowercase password, return invalid password`() {
        val input = "ASDASDASD"
        val result = validatePasswordUseCase(input)

        assertEquals(
            com.leaveloper.authentication_domain.usecase.PasswordResult.INVALID_LOWERCASE,
            result
        )
    }

    @Test
    fun `given no uppercase password, return invalid password`() {
        val input = "asdasdasd"
        val result = validatePasswordUseCase(input)

        assertEquals(
            com.leaveloper.authentication_domain.usecase.PasswordResult.INVALID_UPPERCASE,
            result
        )
    }

    @Test
    fun `given no numbers password, return invalid password`() {
        val input = "ASDASDASDasdasdasd"
        val result = validatePasswordUseCase(input)

        assertEquals(
            com.leaveloper.authentication_domain.usecase.PasswordResult.INVALID_DIGITS,
            result
        )
    }

    @Test
    fun `given invalid password, return invalid password`() {
        val input = "password123"
        val result = validatePasswordUseCase(input)

        assertEquals(
            com.leaveloper.authentication_domain.usecase.PasswordResult.INVALID_UPPERCASE,
            result
        )
    }

    @Test
    fun `given invalid password with no uppercase, return invalid password`() {
        val input = "123123ñloijsdf"
        val result = validatePasswordUseCase(input)

        assertEquals(
            com.leaveloper.authentication_domain.usecase.PasswordResult.INVALID_UPPERCASE,
            result
        )
    }

    @Test
    fun `given valid password with no uppercase, return valid`() {
        val input = "asdASD123"
        val result = validatePasswordUseCase(input)

        assertEquals(
            com.leaveloper.authentication_domain.usecase.PasswordResult.VALID,
            result
        )
    }
}