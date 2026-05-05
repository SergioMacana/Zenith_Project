package com.example.data.local.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

object ZenithNotifier {

    @RequiresApi(Build.VERSION_CODES.O)
    fun pushSystemNotification(
        context: Context,
        notificationId: Int,
        title: String,
        message: String
    ) {
        try {
            val channelId = "zenith_general_channel"

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channel = NotificationChannel(
                channelId,
                "Zenith Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)

            val androidNotification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            notificationManager.notify(notificationId, androidNotification)

        } catch (_: SecurityException) {
        }
    }
}