package com.example.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.fitness.FitnessPrefsManager
import com.example.data.local.notification.NotificationLocalManager
import com.example.domain.model.ExerciseInfo
import com.example.domain.model.ExerciseRoutine
import com.example.domain.model.FitnessHistoryEntry
import com.example.domain.model.NotificationItem
import com.example.domain.model.NotificationType
import com.example.domain.model.TrainingSessionState
import com.example.domain.usecase.fitness.CompleteExerciseUseCase
import com.example.domain.usecase.fitness.GetExerciseRoutineUseCase
import com.example.domain.usecase.fitness.GetExercisesUseCase
import com.example.domain.usecase.fitness.SaveExerciseRoutineUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FitnessViewModel(
    private val getExercisesUseCase: GetExercisesUseCase,
    private val getExerciseRoutineUseCase: GetExerciseRoutineUseCase,
    private val saveExerciseRoutineUseCase: SaveExerciseRoutineUseCase,
    private val completeExerciseUseCase: CompleteExerciseUseCase,
    private val fitnessPrefsManager: FitnessPrefsManager,
    private val notificationLocalManager: NotificationLocalManager
) : ViewModel() {

    private val _exercises = MutableStateFlow<List<ExerciseInfo>>(emptyList())
    val exercises: StateFlow<List<ExerciseInfo>> = _exercises.asStateFlow()

    private val _selectedExercise = MutableStateFlow<ExerciseInfo?>(null)
    val selectedExercise: StateFlow<ExerciseInfo?> = _selectedExercise.asStateFlow()

    private val _routine = MutableStateFlow<ExerciseRoutine?>(null)
    val routine: StateFlow<ExerciseRoutine?> = _routine.asStateFlow()

    private val _session = MutableStateFlow(TrainingSessionState())
    val session: StateFlow<TrainingSessionState> = _session.asStateFlow()

    private var timerJob: Job? = null

    private var workoutCompleted = false

    init {
        loadExercises()
    }

    private fun loadExercises() {
        _exercises.value = getExercisesUseCase()
    }

    fun loadExercise(exerciseId: String) {
        timerJob?.cancel()
        workoutCompleted = false

        val exercise = _exercises.value.find { it.id == exerciseId }
        _selectedExercise.value = exercise
        _routine.value = getExerciseRoutineUseCase(exerciseId)
        _session.value = TrainingSessionState()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleTimer() {
        val current = _session.value
        workoutCompleted = false

        if (current.isRunning) {
            timerJob?.cancel()
            _session.value = current.copy(isRunning = false)
            return
        }

        _session.value = _session.value.copy(isRunning = true)

        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val state = _session.value
                _session.value = state.copy(
                    elapsedSeconds = state.elapsedSeconds + 1
                )
                checkWorkoutCompletion()
            }
        }
    }

    fun resetTimer() {
        workoutCompleted = false
        timerJob?.cancel()
        _session.value = TrainingSessionState()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun restartTimer() {
        workoutCompleted = false

        timerJob?.cancel()
        _session.value = TrainingSessionState(isRunning = true)

        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val state = _session.value
                _session.value = state.copy(
                    elapsedSeconds = state.elapsedSeconds + 1
                )
                checkWorkoutCompletion()
            }
        }
    }

    fun updateRoutine(
        series: Int,
        repetitions: Int,
        workoutSeconds: Int,
        restSeconds: Int
    ) {
        val currentExercise = _selectedExercise.value ?: return

        viewModelScope.launch {
            saveExerciseRoutineUseCase(
                ExerciseRoutine(
                    exerciseId = currentExercise.id,
                    series = series,
                    repetitions = repetitions,
                    workoutSeconds = workoutSeconds,
                    restSeconds = restSeconds
                )
            )

            _routine.value = getExerciseRoutineUseCase(currentExercise.id)
            _session.value = TrainingSessionState()
            timerJob?.cancel()
            workoutCompleted = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkWorkoutCompletion() {
        val routine = _routine.value ?: return
        val elapsed = _session.value.elapsedSeconds

        val totalExpected =
            (routine.series * routine.workoutSeconds) +
                    ((routine.series - 1) * routine.restSeconds)

        if (elapsed >= totalExpected) {
            completeWorkout()
            return
        }

        var remaining = elapsed
        var detectedSeries = 1
        var detectedRep = 1
        var resting = false

        for (seriesIndex in 1..routine.series) {

            if (remaining < routine.workoutSeconds) {
                resting = false

                val secondsPerRep =
                    (routine.workoutSeconds.toFloat() / routine.repetitions.toFloat())

                detectedRep =
                    ((remaining / secondsPerRep).toInt() + 1)
                        .coerceAtMost(routine.repetitions)

                detectedSeries = seriesIndex
                break
            }

            remaining -= routine.workoutSeconds

            if (seriesIndex != routine.series) {
                if (remaining < routine.restSeconds) {
                    resting = true
                    detectedSeries = seriesIndex
                    detectedRep = routine.repetitions
                    break
                }
                remaining -= routine.restSeconds
            }
        }

        _session.value = _session.value.copy(
            currentSeries = detectedSeries,
            currentRepetition = detectedRep,
            isResting = resting,
            totalWorkoutProgress = elapsed.toFloat() / totalExpected.toFloat(),
            seriesProgress = detectedSeries.toFloat() / routine.series.toFloat(),
            repetitionProgress = detectedRep.toFloat() / routine.repetitions.toFloat()
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun completeWorkout() {
        timerJob?.cancel()

        val exerciseId = _selectedExercise.value?.id ?: return
        val now = System.currentTimeMillis()

        val entry = FitnessHistoryEntry(
            id = now,
            exerciseId = exerciseId,
            completedAt = now,
            estimatedCalories = estimateCalories()
        )

        if (workoutCompleted) return
        workoutCompleted = true


        completeExerciseUseCase(entry)

        _routine.value = _routine.value?.copy(
            lastCompletedAt = now
        )

        val calendar = java.util.Calendar.getInstance()
        val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
        val minute = calendar.get(java.util.Calendar.MINUTE)

        val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            .format(java.util.Date(now))

        fitnessPrefsManager.saveLastWorkoutDate(today)
        fitnessPrefsManager.saveUsualWorkoutHour(hour)
        fitnessPrefsManager.saveUsualWorkoutMinute(minute)

        notificationLocalManager.saveNotification(
            NotificationItem(
                id = now.toString(),
                title = "Entrenamiento completado",
                message = "Has finalizado tu rutina de ${_selectedExercise.value?.title ?: "ejercicio"}",
                type = NotificationType.FITNESS,
                timestamp = now,
                displayTime = String.format("%02d:%02d", hour, minute)
            )
        )

        _session.value = TrainingSessionState()
    }

    fun stopTrainingSession() {
        timerJob?.cancel()
        workoutCompleted = false
        _session.value = TrainingSessionState()
    }

    fun onExitTraining() {
        timerJob?.cancel()
        timerJob = null

        _session.value = TrainingSessionState()
    }

    private fun estimateCalories(): Int {
        val elapsed = _session.value.elapsedSeconds
        return (elapsed / 10).coerceAtLeast(5)
    }
}