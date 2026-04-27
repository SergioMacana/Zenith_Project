package com.example.domain.usecase.settings

import com.example.domain.preferences.AppTheme
import com.example.domain.preferences.FontSizeOption
import com.example.domain.repository.UserSettingsRepository

class SaveInitialSettingsUseCase(
    private val repository: UserSettingsRepository
) {
    suspend operator fun invoke(
        userName: String,
        theme: AppTheme,
        fontSize: FontSizeOption
    ) {
        repository.saveInitialSettings(userName, theme, fontSize)
    }
}