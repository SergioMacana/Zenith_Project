package com.example.data.local.notification

import android.content.Context
import android.content.SharedPreferences
import com.example.domain.model.NotificationItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationLocalManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("zenith_notifications", Context.MODE_PRIVATE)

    private val gson = Gson()

    private val _notifications =
        MutableStateFlow(loadNotifications())

    val notifications: StateFlow<List<NotificationItem>> =
        _notifications.asStateFlow()

    fun saveNotification(notification: NotificationItem) {
        val updated = _notifications.value.toMutableList().apply {
            add(0, notification)
        }
        persist(updated)
    }

    fun markAsRead(notificationId: String) {
        val updated = _notifications.value.map { item ->
            if (item.id == notificationId) item.copy(isRead = true) else item
        }
        persist(updated)
    }

    fun clearReadNotifications() {
        val updated = _notifications.value.filter { !it.isRead }
        persist(updated)
    }

    private fun persist(list: List<NotificationItem>) {
        prefs.edit()
            .putString("notifications_json", gson.toJson(list))
            .apply()

        _notifications.value = list
    }

    private fun loadNotifications(): List<NotificationItem> {
        val json = prefs.getString("notifications_json", null) ?: return emptyList()
        val type = object : TypeToken<List<NotificationItem>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    companion object {
        @Volatile
        private var INSTANCE: NotificationLocalManager? = null

        fun getInstance(context: Context): NotificationLocalManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: NotificationLocalManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }
}