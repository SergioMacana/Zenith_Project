package com.example.ui.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.local.fitness.FitnessLocalManager
import com.example.data.local.fitness.FitnessPrefsManager
import com.example.data.local.notification.NotificationLocalManager
import com.example.data.repository.FitnessRepositoryImpl
import com.example.domain.usecase.fitness.CompleteExerciseUseCase
import com.example.domain.usecase.fitness.GetExerciseRoutineUseCase
import com.example.domain.usecase.fitness.GetExercisesUseCase
import com.example.domain.usecase.fitness.SaveExerciseRoutineUseCase
import com.example.ui.viewmodel.FitnessViewModel

class FitnessViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val localManager = FitnessLocalManager(context)
        val repository = FitnessRepositoryImpl(localManager)

        val fitnessPrefsManager = FitnessPrefsManager(context)
        val notificationLocalManager = NotificationLocalManager.getInstance(context)

        val getExercisesUseCase = GetExercisesUseCase(repository)
        val getExerciseRoutineUseCase = GetExerciseRoutineUseCase(repository)
        val saveExerciseRoutineUseCase = SaveExerciseRoutineUseCase(repository)
        val completeExerciseUseCase = CompleteExerciseUseCase(repository)

        return FitnessViewModel(
            getExercisesUseCase = getExercisesUseCase,
            getExerciseRoutineUseCase = getExerciseRoutineUseCase,
            saveExerciseRoutineUseCase = saveExerciseRoutineUseCase,
            completeExerciseUseCase = completeExerciseUseCase,
            fitnessPrefsManager = fitnessPrefsManager,
            notificationLocalManager = notificationLocalManager
        ) as T
    }
}