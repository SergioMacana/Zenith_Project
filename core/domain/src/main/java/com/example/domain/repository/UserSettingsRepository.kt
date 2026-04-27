package com.example.domain.repository

import com.example.domain.model.UserSettings
import com.example.domain.preferences.AppTheme
import com.example.domain.preferences.FontSizeOption
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {

    fun observeSettings(): Flow<UserSettings>

    suspend fun saveInitialSettings(
        userName: String,
        theme: AppTheme,
        fontSize: FontSizeOption
    )

    suspend fun updateTheme(theme: AppTheme)

    suspend fun updateFontSize(fontSize: FontSizeOption)

    suspend fun updateUserName(userName: String)

    suspend fun completeFirstLaunch()
}