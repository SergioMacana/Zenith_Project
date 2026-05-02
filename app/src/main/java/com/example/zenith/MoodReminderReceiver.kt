package com.example.zenith

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import com.example.data.local.notification.NotificationLocalManager
import com.example.domain.model.NotificationItem
import com.example.domain.model.NotificationType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class MoodReminderReceiver : BroadcastReceiver() {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent?) {

        val channelId = "zenith_mood_reminder"

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Zenith Mood Reminder",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val androidNotification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Registro emocional")
            .setContentText("Es momento de registrar cómo te sientes hoy.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1001, androidNotification)

        val displayTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
            .format(Date())

        val internalNotification = NotificationItem(
            id = UUID.randomUUID().toString(),
            title = "Registro emocional",
            message = "Es momento de registrar cómo te sientes hoy.",
            type = NotificationType.MOOD,
            timestamp = System.currentTimeMillis(),
            displayTime = displayTime,
            isRead = false
        )

        NotificationLocalManager.getInstance(context).saveNotification(internalNotification)

        ReminderScheduler.scheduleDailyMoodReminder(context)
    }
}