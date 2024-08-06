package com.leaveloper.home_domain.alarm

import com.leaveloper.home_domain.models.Habit

interface AlarmHandler {
    fun setRecurringAlarm(habit: Habit)
    fun cancel(habit: Habit)
}