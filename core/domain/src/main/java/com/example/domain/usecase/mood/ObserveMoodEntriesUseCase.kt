package com.example.domain.usecase.mood

import com.example.domain.repository.MoodRepository

class ObserveMoodEntriesUseCase(
    private val repository: MoodRepository
) {
    operator fun invoke() = repository.observeMoodEntries()
}