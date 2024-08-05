package com.leaveloper.habitsappcourse.authentication.presentation.login

import com.leaveloper.habitsappcourse.authentication.data.repository.FakeAuthenticationRepository
import com.leaveloper.habitsappcourse.authentication.domain.matcher.EmailMatcher
import com.leaveloper.habitsappcourse.authentication.domain.repository.AuthenticationRepository
import com.leaveloper.habitsappcourse.authentication.domain.usecase.LoginUseCases
import com.leaveloper.habitsappcourse.authentication.domain.usecase.LoginWithEmailUseCase
import com.leaveloper.habitsappcourse.authentication.domain.usecase.ValidateEmailUseCase
import com.leaveloper.habitsappcourse.authentication.domain.usecase.ValidatePasswordUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class LoginViewModelTest {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var authenticationRepository: FakeAuthenticationRepository

    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    @Before
    fun setUp() {
        authenticationRepository = FakeAuthenticationRepository()

        val useCases = LoginUseCases(
            loginWithEmailUseCase = LoginWithEmailUseCase(authenticationRepository),
            validatePasswordUseCase = ValidatePasswordUseCase(),
            validateEmailUseCase = ValidateEmailUseCase(object : EmailMatcher {
                override fun isValid(email: String): Boolean {
                    return email.isNotEmpty()
                }
            })
        )

        loginViewModel = LoginViewModel(useCases, dispatcher)
    }

    @Test
    fun `initial state is empty`() {
        val state = loginViewModel.state

        assertEquals(
            LoginState(
                email = "",
                password = "",
                emailError = null,
                passwordError = null,
                isLoggedIn = false,
                isLoading = false
            ),
            state
        )
    }

    @Test
    fun `given an email, verify the state updates the email`() {
        val initialState = loginViewModel.state.email
        assertEquals(initialState, "")

        loginViewModel.onEvent(LoginEvent.EmailChange("asd@asd.com")) // No importa si es válido o no
        val updatedState = loginViewModel.state.email

        assertEquals(
            "asd@asd.com",
            updatedState
        )
    }

    @Test
    fun `given invalid email, show email error`() {
        loginViewModel.onEvent(LoginEvent.EmailChange(""))
        loginViewModel.onEvent(LoginEvent.Login)

        val state = loginViewModel.state
        assertNotNull(state.emailError)
    }

    @Test
    fun `set valid email, Login, no email error`() {
        loginViewModel.onEvent(LoginEvent.EmailChange("whatever"))
        loginViewModel.onEvent(LoginEvent.Login)
        val state = loginViewModel.state
        assert(state.emailError == null)
    }

    @Test
    fun `set invalid password, Login, show password error`() {
        loginViewModel.onEvent(LoginEvent.PasswordChange("asd"))
        loginViewModel.onEvent(LoginEvent.Login)
        val state = loginViewModel.state
        assertNotNull(state.passwordError)
    }

    @Test
    fun `set valid password, Login, no password error`() {
        loginViewModel.onEvent(LoginEvent.PasswordChange("asdASD123"))
        loginViewModel.onEvent(LoginEvent.Login)
        val state = loginViewModel.state
        assertNull(state.passwordError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `set valid details, Login, start loading and then logs in`() = scope.runTest {
        loginViewModel.onEvent(LoginEvent.EmailChange("whatever"))
        loginViewModel.onEvent(LoginEvent.PasswordChange("asdASD123"))
        loginViewModel.onEvent(LoginEvent.Login)
        var state = loginViewModel.state
        assertNull(state.passwordError)
        assertNull(state.emailError)
        assert(state.isLoading) // Verificar que isLoading = true

        // Ejecuta lo que está dentro de viewModelScope.launch(dispatcher) {}
        // y avanza adentro de los bloques onSuccess {} (fakeError = false) o onFailure {} (fakeError = true)
        advanceUntilIdle()

        state = loginViewModel.state
        assert(state.isLoggedIn) // Verificar que isLoggedIn = true
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `set valid details but server error, Login, start loading and then show error`() = scope.runTest {
        authenticationRepository.fakeError = true

        loginViewModel.onEvent(LoginEvent.EmailChange("whatever"))
        loginViewModel.onEvent(LoginEvent.PasswordChange("asdASD123"))
        loginViewModel.onEvent(LoginEvent.Login)
        var state = loginViewModel.state
        assertNull(state.passwordError)
        assertNull(state.emailError)
        assert(state.isLoading)
        advanceUntilIdle()
        state = loginViewModel.state
        assert(!state.isLoggedIn) // Verificar que isLoggedIn = false
        assertNotNull(state.emailError)
        assert(!state.isLoading)
    }
}