package com.example.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.domain.model.UserSettings
import com.example.domain.preferences.AppTheme
import com.example.domain.preferences.FontSizeOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PrefsManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("zenith_user_settings", Context.MODE_PRIVATE)

    private val _settingsFlow = MutableStateFlow(loadCurrentSettings())
    val settingsFlow: StateFlow<UserSettings> = _settingsFlow.asStateFlow()

    companion object {
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_THEME = "theme"
        private const val KEY_FONT_SIZE = "font_size"
        private const val KEY_FIRST_LAUNCH = "first_launch"
    }

    private fun loadCurrentSettings(): UserSettings {
        val userName = prefs.getString(KEY_USER_NAME, "") ?: ""

        val theme = runCatching {
            AppTheme.valueOf(
                prefs.getString(KEY_THEME, AppTheme.CLASSIC.name)!!
            )
        }.getOrDefault(AppTheme.CLASSIC)

        val fontSize = runCatching {
            FontSizeOption.valueOf(
                prefs.getString(KEY_FONT_SIZE, FontSizeOption.MEDIUM.name)!!
            )
        }.getOrDefault(FontSizeOption.MEDIUM)

        val isFirstLaunch = prefs.getBoolean(KEY_FIRST_LAUNCH, true)

        return UserSettings(
            userName = userName,
            theme = theme,
            fontSize = fontSize,
            isFirstLaunch = isFirstLaunch
        )
    }

    fun observeSettings(): StateFlow<UserSettings> = settingsFlow

    fun saveInitialSettings(
        userName: String,
        theme: AppTheme,
        fontSize: FontSizeOption
    ) {
        prefs.edit()
            .putString(KEY_USER_NAME, userName)
            .putString(KEY_THEME, theme.name)
            .putString(KEY_FONT_SIZE, fontSize.name)
            .putBoolean(KEY_FIRST_LAUNCH, false)
            .apply()

        _settingsFlow.value = loadCurrentSettings()
    }

    fun updateTheme(theme: AppTheme) {
        prefs.edit()
            .putString(KEY_THEME, theme.name)
            .apply()

        _settingsFlow.value = loadCurrentSettings()
    }

    fun updateFontSize(fontSize: FontSizeOption) {
        prefs.edit()
            .putString(KEY_FONT_SIZE, fontSize.name)
            .apply()

        _settingsFlow.value = loadCurrentSettings()
    }

    fun updateUserName(userName: String) {
        prefs.edit()
            .putString(KEY_USER_NAME, userName)
            .apply()

        _settingsFlow.value = loadCurrentSettings()
    }

    fun completeFirstLaunch() {
        prefs.edit()
            .putBoolean(KEY_FIRST_LAUNCH, false)
            .apply()

        _settingsFlow.value = loadCurrentSettings()
    }
}