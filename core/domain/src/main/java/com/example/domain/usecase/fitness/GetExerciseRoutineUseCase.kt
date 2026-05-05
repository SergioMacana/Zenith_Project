package com.example.domain.usecase.fitness

import com.example.domain.model.ExerciseRoutine
import com.example.domain.repository.FitnessRepository

class GetExerciseRoutineUseCase(
    private val repository: FitnessRepository
) {
    operator fun invoke(exerciseId: String): ExerciseRoutine {
        return repository.getRoutineForExercise(exerciseId)
    }
}