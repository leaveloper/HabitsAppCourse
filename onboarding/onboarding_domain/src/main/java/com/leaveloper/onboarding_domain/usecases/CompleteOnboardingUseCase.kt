package com.leaveloper.onboarding_domain.usecases

import com.leaveloper.onboarding_domain.repository.OnboardingRepository

class CompleteOnboardingUseCase(private val repository: OnboardingRepository) {
    operator fun invoke() {
        return repository.completeOnboarding()
    }
}