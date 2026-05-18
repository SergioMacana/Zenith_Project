package com.example.ui.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.data.local.notification.NotificationLocalManager
import com.example.domain.model.NotificationItem
import com.example.domain.model.NotificationType
import com.example.ui.di.ZenithContainer
import com.example.ui.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TaskDailySummaryReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(
        context: Context,
        intent: Intent?
    ) {
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val taskRepository = ZenithContainer(context).taskRepository
                val _selectedDate = MutableStateFlow(DateUtils.todayStart())

                val start = _selectedDate.value
                val end = DateUtils.endOfDay(start)

                // Obtiene las tareas programadas para hoy.
                val tasksForToday = taskRepository.getTasksForDay(start, end)

                if (tasksForToday.isNotEmpty()) {
                    saveDailySummaryNotification(context)
                }

                // Reprogramar la siguiente notificación diaria.
                TaskReminderScheduler.scheduleDailySummary(context)

            } finally {
                pendingResult.finish()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveDailySummaryNotification(
        context: Context
    ) {
        val displayTime = SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        ).format(Date())

        NotificationLocalManager
            .getInstance(context)
            .saveNotification(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    title = "Hoy tienes actividades por hacer",
                    message = "Revisa tus tareas programadas para hoy.",
                    type = NotificationType.TASK,
                    timestamp = System.currentTimeMillis(),
                    displayTime = displayTime
                )
            )
    }
}