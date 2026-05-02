package com.example.ui.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.local.notification.NotificationLocalManager
import com.example.data.repository.NotificationRepositoryImpl
import com.example.domain.usecase.notification.ClearReadNotificationsUseCase
import com.example.domain.usecase.notification.MarkNotificationAsReadUseCase
import com.example.domain.usecase.notification.ObserveNotificationsUseCase
import com.example.domain.usecase.notification.SaveNotificationUseCase
import com.example.ui.viewmodel.NotificationViewModel

class NotificationViewModelFactory (
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val localManager = NotificationLocalManager.getInstance(context)
        val repository = NotificationRepositoryImpl(localManager)

        val observeNotificationsUseCase = ObserveNotificationsUseCase(repository)
        val saveNotificationUseCase = SaveNotificationUseCase(repository)
        val markNotificationAsReadUseCase = MarkNotificationAsReadUseCase(repository)
        val clearReadNotificationsUseCase = ClearReadNotificationsUseCase(repository)

        return NotificationViewModel(
            observeNotificationsUseCase,
            saveNotificationUseCase,
            markNotificationAsReadUseCase,
            clearReadNotificationsUseCase
        ) as T
    }
}