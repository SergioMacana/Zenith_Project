package com.example.data.repository

import com.example.data.local.fitness.FitnessLocalManager
import com.example.domain.catalog.ExerciseCatalog
import com.example.domain.model.ExerciseInfo
import com.example.domain.model.ExerciseRoutine
import com.example.domain.model.FitnessHistoryEntry
import com.example.domain.repository.FitnessRepository
import java.util.Calendar

class FitnessRepositoryImpl (private val localManager: FitnessLocalManager
) : FitnessRepository {

    override fun getExercises(): List<ExerciseInfo> {
        return ExerciseCatalog.exercises
    }

    override fun getRoutineForExercise(exerciseId: String): ExerciseRoutine {
        val routines = localManager.loadRoutines()
        return routines.find { it.exerciseId == exerciseId }
            ?: ExerciseRoutine(exerciseId = exerciseId)
    }

    override fun saveRoutine(routine: ExerciseRoutine) {
        val routines = localManager.loadRoutines().toMutableList()
        val index = routines.indexOfFirst { it.exerciseId == routine.exerciseId }

        if (index >= 0) {
            routines[index] = routine
        } else {
            routines.add(routine)
        }

        localManager.saveRoutines(routines)
    }

    override fun saveCompletedWorkout(entry: FitnessHistoryEntry) {
        val history = localManager.loadHistory().toMutableList()
        history.add(entry)
        localManager.saveHistory(history)

        val currentRoutine = getRoutineForExercise(entry.exerciseId)
        saveRoutine(
            currentRoutine.copy(
                lastCompletedAt = entry.completedAt
            )
        )
    }

    override fun getWeeklyHistory(): List<FitnessHistoryEntry> {
        val now = System.currentTimeMillis()
        val sevenDaysAgo = now - (7L * 24 * 60 * 60 * 1000)

        return localManager.loadHistory()
            .filter { it.completedAt >= sevenDaysAgo }
    }

    override fun hasCompletedWorkoutToday(): Boolean {
        val todayStart = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        return localManager.loadHistory().any { it.completedAt >= todayStart }
    }
}