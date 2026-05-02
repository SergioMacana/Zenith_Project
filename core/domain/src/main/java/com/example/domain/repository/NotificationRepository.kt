package com.example.domain.repository

import com.example.domain.model.NotificationItem
import kotlinx.coroutines.flow.StateFlow

interface NotificationRepository {
    fun observeNotifications(): StateFlow<List<NotificationItem>>
    suspend fun saveNotification(notification: NotificationItem)
    suspend fun markAsRead(notificationId: String)
    suspend fun clearReadNotifications()
}