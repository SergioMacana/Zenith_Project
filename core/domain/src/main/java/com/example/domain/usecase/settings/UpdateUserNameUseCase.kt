package com.example.domain.usecase.settings

import com.example.domain.repository.UserSettingsRepository

class UpdateUserNameUseCase(
    private val repository: UserSettingsRepository
) {
    suspend operator fun invoke(userName: String) {
        repository.updateUserName(userName)
    }
}