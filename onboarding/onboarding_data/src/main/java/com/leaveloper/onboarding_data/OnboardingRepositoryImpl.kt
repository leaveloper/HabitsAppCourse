package com.leaveloper.onboarding_data

import android.content.SharedPreferences

class OnboardingRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : com.leaveloper.onboarding_domain.repository.OnboardingRepository {
    companion object {
        private const val HAS_SEEN_ONBOARDING = "has_seen_onboarding"
    }

    override fun hasSeenOnboarding(): Boolean {
        return sharedPreferences.getBoolean(HAS_SEEN_ONBOARDING, false)
    }

    override fun completeOnboarding() {
        sharedPreferences.edit().putBoolean(HAS_SEEN_ONBOARDING, true).apply()
    }
}