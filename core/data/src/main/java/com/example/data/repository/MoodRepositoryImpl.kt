package com.example.data.repository

import com.example.data.local.mood.MoodLocalManager
import com.example.domain.model.MoodEntry
import com.example.domain.repository.MoodRepository

class MoodRepositoryImpl(
    private val moodLocalManager: MoodLocalManager
) : MoodRepository {

    override fun observeMoodEntries() = moodLocalManager.observeMoodEntries()

    override suspend fun saveMoodEntry(entry: MoodEntry) {
        moodLocalManager.saveMoodEntry(entry)
    }
}