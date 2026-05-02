package com.example.zenith

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.data.local.notification.NotificationLocalManager

class NotificationCleanupReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        NotificationLocalManager.getInstance(context).clearReadNotifications()
        CleanupScheduler.scheduleDailyCleanup(context)
    }
}