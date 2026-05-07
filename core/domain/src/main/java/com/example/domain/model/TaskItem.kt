package com.example.domain.model

data class TaskItem(
    val id: String,
    val title: String,
    val description: String = "",
    val dueDate: Long,
    val dueTimeLabel: String,
    val isCompleted: Boolean = false,
    val reminderEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
