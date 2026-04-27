package com.example.ui.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.local.preferences.PrefsManager
import com.example.data.repository.UserSettingsRepositoryImpl
import com.example.domain.usecase.settings.CompleteFirstLaunchUseCase
import com.example.domain.usecase.settings.ObserveUserSettingsUseCase
import com.example.domain.usecase.settings.SaveInitialSettingsUseCase
import com.example.domain.usecase.settings.UpdateFontSizeUseCase
import com.example.domain.usecase.settings.UpdateThemeUseCase
import com.example.domain.usecase.settings.UpdateUserNameUseCase
import com.example.ui.viewmodel.SettingsViewModel

class SettingsViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val prefsManager = PrefsManager(context.applicationContext)
        val repository = UserSettingsRepositoryImpl(prefsManager)

        val observeUserSettingsUseCase = ObserveUserSettingsUseCase(repository)
        val saveInitialSettingsUseCase = SaveInitialSettingsUseCase(repository)
        val updateThemeUseCase = UpdateThemeUseCase(repository)
        val updateFontSizeUseCase = UpdateFontSizeUseCase(repository)
        val updateUserNameUseCase = UpdateUserNameUseCase(repository)
        val completeFirstLaunchUseCase = CompleteFirstLaunchUseCase(repository)

        return SettingsViewModel(
            observeUserSettingsUseCase,
            saveInitialSettingsUseCase,
            updateThemeUseCase,
            updateFontSizeUseCase,
            updateUserNameUseCase,
            completeFirstLaunchUseCase
        ) as T
    }
}