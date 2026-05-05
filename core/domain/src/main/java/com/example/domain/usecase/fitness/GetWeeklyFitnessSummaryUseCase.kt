package com.example.domain.usecase.fitness

import com.example.domain.model.FitnessHistoryEntry
import com.example.domain.repository.FitnessRepository

class GetWeeklyFitnessSummaryUseCase (
    private val repository: FitnessRepository
) {
    operator fun invoke(): List<FitnessHistoryEntry> {
        return repository.getWeeklyHistory()
    }
}