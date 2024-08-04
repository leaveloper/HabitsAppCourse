package com.leaveloper.habitsappcourse.home.data.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.leaveloper.habitsappcourse.home.data.extension.toTimestamp
import com.leaveloper.habitsappcourse.home.domain.alarm.AlarmHandler
import com.leaveloper.habitsappcourse.home.domain.models.Habit
import java.time.DayOfWeek
import java.time.ZonedDateTime

class AlarmHandlerImpl(private val context: Context) : AlarmHandler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @SuppressLint("ScheduleExactAlarm")
    override fun setRecurringAlarm(habit: Habit) {
        val nextOccurrence = calculateNextOccurrence(habit)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            nextOccurrence.toTimestamp(),
            createPendingIntent(habit, nextOccurrence.dayOfWeek)
        )
    }

    private fun createPendingIntent(habit: Habit, dayOfWeek: DayOfWeek): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.HABIT_ID, habit.id)
        }

        // El requestCode debe ser único
        // Cada habito puede establecerse en más de un día, pero se comparte el id
        // Al concatenar el día, se obtiene un requestCode único
        return PendingIntent.getBroadcast(
            context,
            habit.id.hashCode() * 10 + dayOfWeek.value,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun calculateNextOccurrence(habit: Habit) : ZonedDateTime {
        val today = ZonedDateTime.now()
        var nextOccurrence = ZonedDateTime.of(today.toLocalDate(), habit.reminder, today.zone)

        // Enviar notificacion el mismo día que se verifica
        if (habit.frequency.contains(today.dayOfWeek) && today.isBefore(nextOccurrence)) {
            return nextOccurrence
        }

        do {
            nextOccurrence = nextOccurrence.plusDays(1)
        } while (!habit.frequency.contains(nextOccurrence.dayOfWeek))

        return nextOccurrence
    }

    override fun cancel(habit: Habit) {
        val nextOccurrance = calculateNextOccurrence(habit)
        val pending = createPendingIntent(habit, nextOccurrance.dayOfWeek)
        alarmManager.cancel(pending)
    }
}