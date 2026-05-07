package com.example.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.data.local.notification.NotificationLocalManager
import com.example.domain.model.NotificationItem
import com.example.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.StateFlow

class NotificationRepositoryImpl (
    private val localManager: NotificationLocalManager
) : NotificationRepository {

    override fun observeNotifications(): StateFlow<List<NotificationItem>> {
        return localManager.notifications
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun saveNotification(notification: NotificationItem) {
        localManager.saveNotification(notification)
    }

    override suspend fun markAsRead(notificationId: String) {
        localManager.markAsRead(notificationId)
    }
    override suspend fun clearReadNotifications() {
        localManager.clearReadNotifications()
    }
}