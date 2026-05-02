package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.NotificationItem
import com.example.domain.usecase.notification.ClearReadNotificationsUseCase
import com.example.domain.usecase.notification.MarkNotificationAsReadUseCase
import com.example.domain.usecase.notification.ObserveNotificationsUseCase
import com.example.domain.usecase.notification.SaveNotificationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel (
    private val observeNotificationsUseCase: ObserveNotificationsUseCase,
    private val saveNotificationUseCase: SaveNotificationUseCase,
    private val markNotificationAsReadUseCase: MarkNotificationAsReadUseCase,
    private val clearReadNotificationsUseCase: ClearReadNotificationsUseCase
) : ViewModel() {

    private val _notificationsState =
        MutableStateFlow<List<NotificationItem>>(emptyList())

    val notificationsState: StateFlow<List<NotificationItem>> =
        _notificationsState.asStateFlow()

    init {
        observeNotifications()
    }

    private fun observeNotifications() {
        viewModelScope.launch {
            observeNotificationsUseCase().collect { currentNotifications ->
                _notificationsState.value = currentNotifications
            }
        }
    }

    fun saveNotification(notification: NotificationItem) {
        viewModelScope.launch {
            saveNotificationUseCase(notification)
        }
    }
    fun clearReadNotifications() {
        viewModelScope.launch {
            clearReadNotificationsUseCase()
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            markNotificationAsReadUseCase(notificationId)
        }
    }

    fun getLatestTodayNotification(): NotificationItem? {
        return _notificationsState.value.firstOrNull()
    }

    fun getUnreadNotificationsCount(): Int {
        return _notificationsState.value.count { !it.isRead }
    }
}