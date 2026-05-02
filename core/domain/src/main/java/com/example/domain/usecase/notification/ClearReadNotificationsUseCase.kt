package com.example.domain.usecase.notification

import com.example.domain.repository.NotificationRepository

class ClearReadNotificationsUseCase (
    private val repository: NotificationRepository
) {
    suspend operator fun invoke() {
        repository.clearReadNotifications()
    }
}