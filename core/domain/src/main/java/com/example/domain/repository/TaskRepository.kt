package com.example.domain.repository

import com.example.domain.model.TaskItem

interface TaskRepository {

    suspend fun insertTask(task: TaskItem)

    suspend fun updateTask(task: TaskItem)

    suspend fun deleteTask(taskId: String)

    suspend fun completeTask(taskId: String)

    suspend fun getAllTasks(): List<TaskItem>

    suspend fun getTasksForDay(dayStart: Long, dayEnd: Long): List<TaskItem>

    suspend fun getUpcomingTasks(limit: Int = 5): List<TaskItem>

    suspend fun getTaskById(taskId: String): TaskItem?
}