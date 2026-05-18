package com.example.ui.receivers

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.domain.model.TaskItem
import java.util.Calendar
import kotlin.jvm.java

object TaskReminderScheduler {

    private const val DAILY_SUMMARY_REQUEST_CODE = 5000
    private const val REMINDER_OFFSET = 100000

    fun scheduleTaskNotifications(
        context: Context,
        task: TaskItem
    ) {
        val dueDate = task.dueDate ?: return

        // No programar tareas completadas
        if (task.isCompleted) return

        // No programar fechas pasadas
        if (dueDate <= System.currentTimeMillis()) return

        // Programar notificación exacta
        scheduleSingleNotification(
            context = context,
            triggerAtMillis = dueDate,
            task = task,
            isReminder = false
        )

        // Programar recordatorio 2 horas antes
        val reminderTime = dueDate - (2 * 60 * 60 * 1000L)

        if (reminderTime > System.currentTimeMillis()) {
            scheduleSingleNotification(
                context = context,
                triggerAtMillis = reminderTime,
                task = task,
                isReminder = true
            )
        }
    }

    //Cancela las notificaciones asociadas a una tarea.
    fun cancelTaskNotifications(
        context: Context,
        task: TaskItem
    ) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Cancelar recordatorio
        val reminderIntent = createTaskPendingIntent(
            context = context,
            task = task,
            isReminder = true,
            flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(reminderIntent)
        reminderIntent.cancel()

        // Cancelar notificación exacta
        val exactIntent = createTaskPendingIntent(
            context = context,
            task = task,
            isReminder = false,
            flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(exactIntent)
        exactIntent.cancel()
    }

    //Programa la notificación diaria general a las 06:00 AM.
    fun scheduleDailySummary(context: Context) {

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(
            context,
            TaskDailySummaryReceiver::class.java
        )

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            DAILY_SUMMARY_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 6)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    //Cancela la notificación diaria general.
    @SuppressLint("ServiceCast")
    fun cancelDailySummary(context: Context) {

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(
            context,
            TaskDailySummaryReceiver::class.java
        )

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            DAILY_SUMMARY_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    // Reprograma todas las tareas pendientes.
    fun rescheduleAllTasks(
        context: Context,
        tasks: List<TaskItem>
    ) {
        scheduleDailySummary(context)

        tasks.forEach { task ->
            scheduleTaskNotifications(context, task)
        }
    }

    // Programa una única alarma.
    private fun scheduleSingleNotification(
        context: Context,
        triggerAtMillis: Long,
        task: TaskItem,
        isReminder: Boolean
    ) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val pendingIntent = createTaskPendingIntent(
            context = context,
            task = task,
            isReminder = isReminder,
            flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }

    // Crea el PendingIntent para TaskReminderReceiver.
    private fun createTaskPendingIntent(
        context: Context,
        task: TaskItem,
        isReminder: Boolean,
        flags: Int
    ): PendingIntent {

        val intent = Intent(
            context,
            TaskReminderReceiver::class.java
        ).apply {
            putExtra("task_id", task.id)
            putExtra("is_reminder", isReminder)
        }

        val requestCode =
            if (isReminder) {
                task.id.hashCode() + REMINDER_OFFSET
            } else {
                task.id.hashCode()
            }

        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            flags
        )
    }
}