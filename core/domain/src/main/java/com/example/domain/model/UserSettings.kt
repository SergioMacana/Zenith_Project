package com.example.domain.model

import com.example.domain.preferences.AppTheme
import com.example.domain.preferences.FontSizeOption

data class UserSettings(
    val userName: String = "",
    val theme: AppTheme = AppTheme.CLASSIC,
    val fontSize: FontSizeOption = FontSizeOption.MEDIUM,
    val isFirstLaunch: Boolean = true
)
