package com.example.domain.model

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val timestamp: Long,
    val displayTime: String,
    val isRead: Boolean = false
)
