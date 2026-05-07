package com.example.data.repository

import com.example.data.local.room.dao.TaskDao
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.domain.model.TaskItem
import com.example.domain.repository.TaskRepository

class TaskRepositoryImpl(
    private val taskDao: TaskDao
) : TaskRepository {

    override suspend fun insertTask(task: TaskItem) {
        taskDao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: TaskItem) {
        taskDao.updateTask(task.toEntity())
    }

    override suspend fun deleteTask(taskId: String) {
        taskDao.deleteTask(taskId)
    }

    override suspend fun completeTask(taskId: String) {
        taskDao.completeTask(taskId)
    }

    override suspend fun getAllTasks(): List<TaskItem> {
        return taskDao.getAllPendingTasks().map { it.toDomain() }
    }

    override suspend fun getTasksForDay(dayStart: Long, dayEnd: Long): List<TaskItem> {
        return taskDao.getTasksForDay(dayStart, dayEnd).map { it.toDomain() }
    }

    override suspend fun getUpcomingTasks(limit: Int): List<TaskItem> {
        return taskDao.getUpcomingTasks(
            currentTime = System.currentTimeMillis(),
            limit = limit
        ).map { it.toDomain() }
    }

    override suspend fun getTaskById(taskId: String): TaskItem? {
        return taskDao.getTaskById(taskId)?.toDomain()
    }
}