package com.example.domain.model

data class MoodEntry(
    val moodId: String,
    val moodLabel: String,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
