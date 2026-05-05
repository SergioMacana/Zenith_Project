package com.example.domain.usecase.fitness

import com.example.domain.model.ExerciseInfo
import com.example.domain.repository.FitnessRepository

class GetExercisesUseCase (
    private val repository: FitnessRepository
) {
    operator fun invoke(): List<ExerciseInfo> {
        return repository.getExercises()
    }
}