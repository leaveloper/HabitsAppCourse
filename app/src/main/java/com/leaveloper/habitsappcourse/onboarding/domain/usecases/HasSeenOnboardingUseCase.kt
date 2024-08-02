package com.leaveloper.habitsappcourse.onboarding.domain.usecases

import com.leaveloper.habitsappcourse.onboarding.domain.repository.OnboardingRepository

class HasSeenOnboardingUseCase(private val repository: OnboardingRepository) {
    operator fun invoke(): Boolean {
        return repository.hasSeenOnboarding()
    }
}