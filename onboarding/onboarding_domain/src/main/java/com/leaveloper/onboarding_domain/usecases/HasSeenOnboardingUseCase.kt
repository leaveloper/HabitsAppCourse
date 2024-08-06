package com.leaveloper.onboarding_domain.usecases

import com.leaveloper.onboarding_domain.repository.OnboardingRepository

class HasSeenOnboardingUseCase(private val repository: OnboardingRepository) {
    operator fun invoke(): Boolean {
        return repository.hasSeenOnboarding()
    }
}