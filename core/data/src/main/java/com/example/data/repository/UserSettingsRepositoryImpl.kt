package com.example.data.repository

import com.example.data.local.preferences.PrefsManager
import com.example.domain.preferences.AppTheme
import com.example.domain.preferences.FontSizeOption
import com.example.domain.repository.UserSettingsRepository

class UserSettingsRepositoryImpl(
    private val prefsManager: PrefsManager
) : UserSettingsRepository {

    override fun observeSettings() = prefsManager.observeSettings()

    override suspend fun saveInitialSettings(
        userName: String,
        theme: AppTheme,
        fontSize: FontSizeOption
    ) {
        prefsManager.saveInitialSettings(userName, theme, fontSize)
    }

    override suspend fun updateTheme(theme: AppTheme) {
        prefsManager.updateTheme(theme)
    }

    override suspend fun updateFontSize(fontSize: FontSizeOption) {
        prefsManager.updateFontSize(fontSize)
    }

    override suspend fun updateUserName(userName: String) {
        prefsManager.updateUserName(userName)
    }

    override suspend fun completeFirstLaunch() {
        prefsManager.completeFirstLaunch()
    }
}