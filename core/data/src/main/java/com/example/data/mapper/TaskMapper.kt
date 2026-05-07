package com.example.data.mapper

import com.example.data.local.room.entity.TaskItemEntity
import com.example.domain.model.TaskItem

fun TaskItemEntity.toDomain(): TaskItem {
    return TaskItem(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        dueDate = dueDate,
        dueTimeLabel = dueTimeLabel,
        reminderEnabled = reminderEnabled,
        createdAt = createdAt
    )
}

fun TaskItem.toEntity(): TaskItemEntity {
    return TaskItemEntity(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        dueDate = dueDate,
        dueTimeLabel = dueTimeLabel,
        reminderEnabled = reminderEnabled,
        createdAt = createdAt
    )
}