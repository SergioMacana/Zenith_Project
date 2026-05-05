package com.example.domain.model

data class FitnessHistoryEntry(
    val id: Long,
    val exerciseId: String,
    val completedAt: Long,
    val estimatedCalories: Int
)
