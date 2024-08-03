package com.leaveloper.habitsappcourse.authentication.presentation.signup

sealed interface SignupEvent {
    data class EmailChange(val email: String) : SignupEvent
    data class PasswordChange(val password: String) : SignupEvent
    object SignIn: SignupEvent
    object Signup: SignupEvent
}