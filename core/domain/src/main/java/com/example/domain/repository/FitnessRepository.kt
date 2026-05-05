package com.example.domain.repository

import com.example.domain.model.ExerciseInfo
import com.example.domain.model.ExerciseRoutine
import com.example.domain.model.FitnessHistoryEntry

interface FitnessRepository {
    fun getExercises(): List<ExerciseInfo>

    fun getRoutineForExercise(exerciseId: String): ExerciseRoutine

    fun saveRoutine(routine: ExerciseRoutine)

    fun saveCompletedWorkout(entry: FitnessHistoryEntry)

    fun getWeeklyHistory(): List<FitnessHistoryEntry>

    fun hasCompletedWorkoutToday(): Boolean
}