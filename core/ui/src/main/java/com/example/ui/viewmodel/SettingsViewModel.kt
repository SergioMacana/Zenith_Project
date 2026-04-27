package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.UserSettings
import com.example.domain.preferences.AppTheme
import com.example.domain.preferences.FontSizeOption
import com.example.domain.usecase.settings.CompleteFirstLaunchUseCase
import com.example.domain.usecase.settings.ObserveUserSettingsUseCase
import com.example.domain.usecase.settings.SaveInitialSettingsUseCase
import com.example.domain.usecase.settings.UpdateFontSizeUseCase
import com.example.domain.usecase.settings.UpdateThemeUseCase
import com.example.domain.usecase.settings.UpdateUserNameUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val observeUserSettingsUseCase: ObserveUserSettingsUseCase,
    private val saveInitialSettingsUseCase: SaveInitialSettingsUseCase,
    private val updateThemeUseCase: UpdateThemeUseCase,
    private val updateFontSizeUseCase: UpdateFontSizeUseCase,
    private val updateUserNameUseCase: UpdateUserNameUseCase,
    private val completeFirstLaunchUseCase: CompleteFirstLaunchUseCase
) : ViewModel() {

    private val _settingsState = MutableStateFlow(UserSettings())
    val settingsState: StateFlow<UserSettings> = _settingsState.asStateFlow()

    init {
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            observeUserSettingsUseCase().collectLatest { currentSettings: UserSettings ->
                _settingsState.value = currentSettings
            }
        }
    }

    fun saveInitialSettings(
        userName: String,
        theme: AppTheme,
        fontSize: FontSizeOption
    ) {
        viewModelScope.launch {
            saveInitialSettingsUseCase(userName, theme, fontSize)
        }
    }

    fun updateTheme(theme: AppTheme) {
        viewModelScope.launch {
            updateThemeUseCase(theme)
        }
    }

    fun updateFontSize(fontSize: FontSizeOption) {
        viewModelScope.launch {
            updateFontSizeUseCase(fontSize)
        }
    }

    fun updateUserName(userName: String) {
        viewModelScope.launch {
            updateUserNameUseCase(userName)
        }
    }

    fun completeFirstLaunch() {
        viewModelScope.launch {
            completeFirstLaunchUseCase()
        }
    }
}