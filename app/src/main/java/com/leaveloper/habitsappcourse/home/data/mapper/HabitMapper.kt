package com.leaveloper.habitsappcourse.home.data.mapper

import com.leaveloper.habitsappcourse.home.data.extension.toStartOfDateTimestamp
import com.leaveloper.habitsappcourse.home.data.extension.toTimestamp
import com.leaveloper.habitsappcourse.home.data.extension.toZonedDateTime
import com.leaveloper.habitsappcourse.home.data.extension.toZonedDateTime
import com.leaveloper.habitsappcourse.home.data.local.entity.HabitEntity
import com.leaveloper.habitsappcourse.home.domain.models.Habit
import java.time.DayOfWeek

fun HabitEntity.toDomain(): Habit {
    return Habit(
        id = this.id,
        name = this.name,
        frequency = this.frequency.map { DayOfWeek.of(it) },
        completedDates = this.completedDates.map {
            it.toZonedDateTime().toLocalDate()
        },
        reminder = this.reminder.toZonedDateTime().toLocalTime(),
        startDate = this.startDate.toZonedDateTime()
    )
}

fun Habit.toEntity(): HabitEntity {
    return HabitEntity(
        id = this.id,
        name = this.name,
        frequency = this.frequency.map { it.value },
        completedDates = this.completedDates.map {
            it.toZonedDateTime().toTimestamp()
        },
        reminder = this.reminder.toZonedDateTime().toTimestamp(),
        startDate = this.startDate.toStartOfDateTimestamp()
    )
}