package com.example.domain.repository

import com.example.domain.model.MoodEntry
import kotlinx.coroutines.flow.StateFlow

interface MoodRepository {

    fun observeMoodEntries(): StateFlow<List<MoodEntry>>

    suspend fun saveMoodEntry(entry: MoodEntry)
}