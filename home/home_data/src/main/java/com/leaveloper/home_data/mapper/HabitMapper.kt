package com.leaveloper.home_data.mapper

import com.leaveloper.home_data.extension.toStartOfDateTimestamp
import com.leaveloper.home_data.extension.toTimestamp
import com.leaveloper.home_data.extension.toZonedDateTime
import com.leaveloper.home_data.extension.toZonedDateTime
import com.leaveloper.home_data.local.entity.HabitEntity
import com.leaveloper.home_data.local.entity.HabitSyncEntity
import com.leaveloper.home_data.remote.dto.HabitDto
import com.leaveloper.home_data.remote.dto.HabitResponse
import com.leaveloper.home_domain.models.Habit
import java.time.DayOfWeek

fun HabitEntity.toDomain(): com.leaveloper.home_domain.models.Habit {
    return com.leaveloper.home_domain.models.Habit(
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

fun com.leaveloper.home_domain.models.Habit.toEntity(): HabitEntity {
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

fun com.leaveloper.home_domain.models.Habit.toDto(): HabitResponse {
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

fun HabitResponse.toDomain(): List<com.leaveloper.home_domain.models.Habit> {
    return this.entries.map {
        val id = it.key
        val dto = it.value

        com.leaveloper.home_domain.models.Habit(
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

fun com.leaveloper.home_domain.models.Habit.toSyncEntity() : HabitSyncEntity {
    return HabitSyncEntity(id)
}