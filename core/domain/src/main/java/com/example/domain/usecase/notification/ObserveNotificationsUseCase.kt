package com.example.domain.usecase.notification

import com.example.domain.repository.NotificationRepository

class ObserveNotificationsUseCase (
    private val repository: NotificationRepository
    ) {
        operator fun invoke() = repository.observeNotifications()
}