package com.example.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_items")
data class TaskItemEntity(

    @PrimaryKey
    val id: String,

    val title: String,
    val description: String = "",

    val dueDate: Long,
    val dueTimeLabel: String,

    val isCompleted: Boolean = false,

    val reminderEnabled: Boolean = true,

    val createdAt: Long = System.currentTimeMillis()
)