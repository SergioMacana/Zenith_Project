package com.example.zenith.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.data.local.fitness.FitnessPrefsManager
import com.example.data.local.notification.NotificationLocalManager
import com.example.domain.model.NotificationItem
import com.example.domain.model.NotificationType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class FitnessMorningReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val prefs = FitnessPrefsManager(context)
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) .format(Date())
        val lastWorkout = prefs.getLastWorkoutDate()

        if (lastWorkout == today) {
            FitnessReminderScheduler.scheduleMorningReminder(context)
            return
        }

        val displayTime = SimpleDateFormat("hh:mm a", Locale.getDefault()) .format(Date())

        NotificationLocalManager.getInstance(context).saveNotification(
            NotificationItem(
                id = UUID.randomUUID().toString(),
                title = "Hora de activarte",
                message = "Aún no has realizado ejercicio hoy. Dedica unos minutos a tu bienestar.",
                type = NotificationType.FITNESS,
                timestamp = System.currentTimeMillis(),
                displayTime = displayTime
            )
        )
        FitnessReminderScheduler.scheduleMorningReminder(context)
    }
}