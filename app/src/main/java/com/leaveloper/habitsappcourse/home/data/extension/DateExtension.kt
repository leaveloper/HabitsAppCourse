package com.leaveloper.habitsappcourse.home.data.extension

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

fun ZonedDateTime.toStartOfDateTimestamp(): Long {
    // (ChronoUnit.DAYS) regresa el inicio del día (00:00)
    return truncatedTo(ChronoUnit.DAYS).toEpochSecond() * 1000
}

fun Long.toZonedDateTime(): ZonedDateTime {
    return ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(this),
        ZoneId.systemDefault() // Zona horaria del celular
    )
}

fun ZonedDateTime.toTimestamp(): Long {
    return this.toInstant().toEpochMilli()
}

fun LocalDate.toZonedDateTime(): ZonedDateTime {
    return this.atStartOfDay(ZoneId.systemDefault())
}

fun LocalTime.toZonedDateTime(): ZonedDateTime {
    return this.atDate(LocalDate.now()).atZone(ZoneId.systemDefault())
}