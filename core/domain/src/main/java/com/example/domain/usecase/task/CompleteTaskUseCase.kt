package com.example.domain.usecase.task

import com.example.domain.repository.TaskRepository

class CompleteTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: String) {
        repository.completeTask(taskId)
    }
}