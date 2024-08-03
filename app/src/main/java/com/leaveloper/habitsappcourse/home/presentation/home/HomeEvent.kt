package com.leaveloper.habitsappcourse.home.presentation.home

import com.leaveloper.habitsappcourse.home.presentation.home.domain.models.Habit
import java.time.ZonedDateTime

sealed interface HomeEvent {
    data class ChangeDate(val date: ZonedDateTime) : HomeEvent
    data class CompleteHabit(val habit: Habit) : HomeEvent
}