package com.example.domain.usecase.task

import com.example.domain.model.TaskItem
import com.example.domain.repository.TaskRepository

class GetUpcomingTasksUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(limit: Int = 5): List<TaskItem> {
        return repository.getUpcomingTasks(limit)
    }
}