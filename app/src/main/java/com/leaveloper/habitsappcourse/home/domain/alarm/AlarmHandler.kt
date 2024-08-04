package com.leaveloper.habitsappcourse.home.domain.alarm

import com.leaveloper.habitsappcourse.home.domain.models.Habit

interface AlarmHandler {
    fun setRecurringAlarm(habit: Habit)
    fun cancel(habit: Habit)
}