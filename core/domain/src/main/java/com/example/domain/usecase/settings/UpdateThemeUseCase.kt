package com.example.domain.usecase.settings

import com.example.domain.preferences.AppTheme
import com.example.domain.repository.UserSettingsRepository

class UpdateThemeUseCase(
    private val repository: UserSettingsRepository
) {
    suspend operator fun invoke(theme: AppTheme) {
        repository.updateTheme(theme)
    }
}