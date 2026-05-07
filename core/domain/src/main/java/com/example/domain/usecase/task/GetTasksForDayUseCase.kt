package com.example.domain.usecase.task

import com.example.domain.model.TaskItem
import com.example.domain.repository.TaskRepository

class GetTasksForDayUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(dayStart: Long, dayEnd: Long): List<TaskItem> {
        return repository.getTasksForDay(dayStart, dayEnd)
    }
}