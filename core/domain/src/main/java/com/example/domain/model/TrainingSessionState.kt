package com.example.domain.model

data class TrainingSessionState(
    val elapsedSeconds: Int = 0,
    val isRunning: Boolean = false,

    val currentSeries: Int = 1,
    val currentRepetition: Int = 1,

    val isResting: Boolean = false,

    val totalWorkoutProgress: Float = 0f,
    val seriesProgress: Float = 0f,
    val repetitionProgress: Float = 0f
)
