package com.example.domain.usecase.settings

import com.example.domain.repository.UserSettingsRepository

class CompleteFirstLaunchUseCase(
    private val repository: UserSettingsRepository
) {
    suspend operator fun invoke() {
        repository.completeFirstLaunch()
    }
}