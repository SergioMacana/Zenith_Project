package com.example.domain.usecase.task

import com.example.domain.model.TaskItem
import com.example.domain.repository.TaskRepository

class CreateTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: TaskItem) {
        repository.insertTask(task)
    }
}