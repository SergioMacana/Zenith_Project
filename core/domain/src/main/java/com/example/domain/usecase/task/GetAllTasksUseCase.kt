package com.example.domain.usecase.task

import com.example.domain.model.TaskItem
import com.example.domain.repository.TaskRepository

class GetAllTasksUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(): List<TaskItem> {
        return repository.getAllTasks()
    }
}