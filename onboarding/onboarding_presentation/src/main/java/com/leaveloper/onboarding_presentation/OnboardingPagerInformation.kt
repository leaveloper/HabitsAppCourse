package com.leaveloper.onboarding_presentation

import androidx.annotation.DrawableRes

data class OnboardingPagerInformation(
    val title: String,
    val subtitle: String,
    // Solo acepta Ids que estén dentro de la carpeta 'drawable'
    @DrawableRes val image: Int
)
