package com.example.domain.model

data class ExerciseRoutine(
    val exerciseId: String,
    val series: Int = 3,
    val repetitions: Int = 12,
    val workoutSeconds: Int = 60,
    val restSeconds: Int = 120,
    val lastCompletedAt: Long? = null,
    val usualTrainingHour: Int = 9
)
