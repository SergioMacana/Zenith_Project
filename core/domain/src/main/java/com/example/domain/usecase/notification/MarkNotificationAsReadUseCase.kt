package com.example.domain.usecase.notification

import com.example.domain.repository.NotificationRepository

class MarkNotificationAsReadUseCase (
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: String) {
        repository.markAsRead(notificationId)
    }
}