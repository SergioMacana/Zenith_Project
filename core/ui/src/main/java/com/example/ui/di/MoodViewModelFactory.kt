package com.example.ui.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.local.mood.MoodLocalManager
import com.example.data.repository.MoodRepositoryImpl
import com.example.domain.usecase.mood.ObserveMoodEntriesUseCase
import com.example.domain.usecase.mood.SaveMoodEntryUseCase
import com.example.ui.viewmodel.MoodViewModel

class MoodViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val localManager = MoodLocalManager(context)
        val repository = MoodRepositoryImpl(localManager)

        val observeUseCase = ObserveMoodEntriesUseCase(repository)
        val saveUseCase = SaveMoodEntryUseCase(repository)

        return MoodViewModel(
            observeMoodEntriesUseCase = observeUseCase,
            saveMoodEntryUseCase = saveUseCase
        ) as T
    }
}