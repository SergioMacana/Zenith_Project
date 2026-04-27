package com.example.domain.usecase.settings

import com.example.domain.preferences.FontSizeOption
import com.example.domain.repository.UserSettingsRepository

class UpdateFontSizeUseCase(
    private val repository: UserSettingsRepository
) {
    suspend operator fun invoke(fontSize: FontSizeOption) {
        repository.updateFontSize(fontSize)
    }
}