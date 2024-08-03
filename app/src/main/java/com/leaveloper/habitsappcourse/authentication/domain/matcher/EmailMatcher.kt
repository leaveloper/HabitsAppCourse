package com.leaveloper.habitsappcourse.authentication.domain.matcher

interface EmailMatcher {
    fun isValid(email: String): Boolean
}