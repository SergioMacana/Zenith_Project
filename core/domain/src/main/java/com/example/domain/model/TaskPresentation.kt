package com.example.domain.model

data class TaskPresentation(
    val id: String,
    val title: String,
    val description: String,
    val dueDate: Long,
    val time: String,
    val isCompleted: Boolean,
    val reminderEnabled: Boolean,
    val createdAt: Long
)

fun TaskItem.toPresentation(): TaskPresentation {
    return TaskPresentation(
        id = id,
        title = title,
        description = description,
        dueDate = dueDate,
        time = dueTimeLabel,
        isCompleted = isCompleted,
        reminderEnabled = reminderEnabled,
        createdAt = createdAt
    )
}

fun TaskPresentation.dayLabel(): String {
    val formatter = java.text.SimpleDateFormat("dd", java.util.Locale.getDefault())
    return formatter.format(java.util.Date(dueDate))
}

fun TaskPresentation.monthLabel(): String {
    val formatter = java.text.SimpleDateFormat("MMMM", java.util.Locale.getDefault())
    return formatter.format(java.util.Date(dueDate))
}