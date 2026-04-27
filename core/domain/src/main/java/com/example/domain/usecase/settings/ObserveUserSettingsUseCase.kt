package com.example.domain.usecase.settings

import com.example.domain.repository.UserSettingsRepository

class ObserveUserSettingsUseCase(
    private val repository: UserSettingsRepository
) {
    operator fun invoke() = repository.observeSettings()
}