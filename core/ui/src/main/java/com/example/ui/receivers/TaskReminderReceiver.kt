package com.example.ui.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.data.local.notification.NotificationLocalManager
import com.example.domain.model.NotificationItem
import com.example.domain.model.NotificationType
import com.example.domain.model.TaskItem
import com.example.ui.di.ZenithContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class TaskReminderReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(
        context: Context,
        intent: Intent?
    ) {
        val taskId = intent?.getStringExtra("task_id") ?: return
        val isReminder = intent.getBooleanExtra("is_reminder", false)

        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val taskRepository = ZenithContainer(context).taskRepository
                val task = taskRepository.getTaskById(taskId)

                if (task == null) return@launch

                // No mostrar si la tarea ya fue completada
                if (task.isCompleted) return@launch

                saveTaskNotification(
                    context = context,
                    task = task,
                    isReminder = isReminder
                )
            } finally {
                pendingResult.finish()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveTaskNotification(
        context: Context,
        task: TaskItem,
        isReminder: Boolean
    ) {
        val displayTime = SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        ).format(Date(task.dueDate ?: System.currentTimeMillis()))

        val message = buildString {
            append(task.title)

            if (task.description.isNotBlank()) {
                append(" - ")
                append(task.description)
            }

            append(" - ")
            append(displayTime)
        }

        val title =
            if (isReminder) {
                "Recordatorio de tarea"
            } else {
                "Actividad programada"
            }

        NotificationLocalManager
            .getInstance(context)
            .saveNotification(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    message = message,
                    type = NotificationType.TASK,
                    timestamp = System.currentTimeMillis(),
                    displayTime = SimpleDateFormat(
                        "hh:mm a",
                        Locale.getDefault()
                    ).format(Date())
                )
            )
    }
}