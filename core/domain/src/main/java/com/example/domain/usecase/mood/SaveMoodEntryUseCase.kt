package com.example.domain.usecase.mood

import com.example.domain.model.MoodEntry
import com.example.domain.repository.MoodRepository

class SaveMoodEntryUseCase(
    private val repository: MoodRepository
) {
    suspend operator fun invoke(entry: MoodEntry) {
        repository.saveMoodEntry(entry)
    }
}