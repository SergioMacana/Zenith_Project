package com.example.domain.usecase.fitness

import com.example.domain.model.FitnessHistoryEntry
import com.example.domain.repository.FitnessRepository

class CompleteExerciseUseCase (
    private val repository: FitnessRepository
) {
    operator fun invoke(entry: FitnessHistoryEntry) {
        repository.saveCompletedWorkout(entry)
    }
}