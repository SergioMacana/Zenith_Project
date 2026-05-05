package com.example.domain.usecase.fitness

import com.example.domain.repository.FitnessRepository

class HasCompletedWorkoutTodayUseCase (
    private val repository: FitnessRepository
) {
    operator fun invoke(): Boolean {
        return repository.hasCompletedWorkoutToday()
    }
}