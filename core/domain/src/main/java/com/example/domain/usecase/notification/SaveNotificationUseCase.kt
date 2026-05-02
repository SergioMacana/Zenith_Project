package com.example.domain.usecase.notification

import com.example.domain.model.NotificationItem
import com.example.domain.repository.NotificationRepository

class SaveNotificationUseCase (
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notification: NotificationItem) {
        repository.saveNotification(notification)
    }
}