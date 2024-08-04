package com.leaveloper.habitsappcourse.home.data.mapper

import com.leaveloper.habitsappcourse.home.data.extension.toStartOfDateTimestamp
import com.leaveloper.habitsappcourse.home.data.extension.toTimestamp
import com.leaveloper.habitsappcourse.home.data.extension.toZonedDateTime
import com.leaveloper.habitsappcourse.home.data.extension.toZonedDateTime
import com.leaveloper.habitsappcourse.home.data.local.entity.HabitEntity
import com.leaveloper.habitsappcourse.home.data.local.entity.HabitSyncEntity
import com.leaveloper.habitsappcourse.home.data.remote.dto.HabitDto
import com.leaveloper.habitsappcourse.home.data.remote.dto.HabitResponse
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

fun Habit.toDto(): HabitResponse {
    val dto = HabitDto(
        name = this.name,
        frequency = this.frequency.map { it.value },
        completedDates = this.completedDates.map {
            it.toZonedDateTime().toTimestamp()
        },
        reminder = this.reminder.toZonedDateTime().toTimestamp(),
        startDate = this.startDate.toStartOfDateTimestamp()
    )

    //Map<String, HabitDto>
    return mapOf(id to dto)
}

fun HabitResponse.toDomain(): List<Habit> {
    return this.entries.map {
        val id = it.key
        val dto = it.value

        Habit(
            id = id,
            name = dto.name,
            frequency = dto.frequency.map { DayOfWeek.of(it) },
            completedDates = dto.completedDates?.map {
                it.toZonedDateTime().toLocalDate()
            } ?: emptyList(),
            reminder = dto.reminder.toZonedDateTime().toLocalTime(),
            startDate = dto.startDate.toZonedDateTime()
        )
    }
}

fun Habit.toSyncEntity() : HabitSyncEntity {
    return HabitSyncEntity(id)
}