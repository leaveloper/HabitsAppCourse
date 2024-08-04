package com.leaveloper.habitsappcourse.home.data.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.leaveloper.habitsappcourse.R
import com.leaveloper.habitsappcourse.home.domain.repository.HomeRepository
import javax.inject.Inject
import com.leaveloper.habitsappcourse.home.data.extension.goAsync
import com.leaveloper.habitsappcourse.home.domain.alarm.AlarmHandler
import com.leaveloper.habitsappcourse.home.domain.models.Habit
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val HABIT_ID = "habit_id"
        private const val CHANNEL_ID = "habbits_channel"
    }

    // El repositorio no se puede inyectar desde el constructor de la clase
    // porque BroadcastReceiver no se puede instanciar (Por ejemplo, en HomeModule)
    @Inject
    lateinit var repository: HomeRepository

    @Inject
    lateinit var alarmHandler: AlarmHandler

    override fun onReceive(context: Context?, intent: Intent?) = goAsync {
        if (context == null || intent == null) return@goAsync

        val id = intent.getStringExtra(HABIT_ID) ?: return@goAsync
        val habit = repository.getHabitById(id)
        createNotificationChannel(context)

        // Si el habito se completó antes de la hora establecida
        // no muestra la notificación
        if (!habit.completedDates.contains(LocalDate.now())) {
            showNotification(context, habit)
        }

        alarmHandler.setRecurringAlarm(habit)
    }

    private fun showNotification(context: Context, habit: Habit) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val notification = NotificationCompat
            .Builder(context, CHANNEL_ID)
            .setContentTitle(habit.name)
            .setSmallIcon((R.drawable.ic_notification))
            .setAutoCancel(true)
            .build()

        notificationManager.notify(habit.id.hashCode(), notification)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Habit Reminder Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Get your habits reminder!"

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}