package com.example.domain.usecase.fitness

import com.example.domain.model.ExerciseRoutine
import com.example.domain.repository.FitnessRepository

class SaveExerciseRoutineUseCase (
    private val repository: FitnessRepository
) {
    operator fun invoke(routine: ExerciseRoutine) {
        repository.saveRoutine(routine)
    }
}