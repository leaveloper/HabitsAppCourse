package com.leaveloper.habitsappcourse.onboarding.domain.usecases

import com.leaveloper.habitsappcourse.onboarding.domain.repository.OnboardingRepository

class CompleteOnboardingUseCase(private val repository: OnboardingRepository) {
    operator fun invoke() {
        return repository.completeOnboarding()
    }
}