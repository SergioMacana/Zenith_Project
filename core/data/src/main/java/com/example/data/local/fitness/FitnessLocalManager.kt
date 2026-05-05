package com.example.data.local.fitness

import android.content.Context
import com.example.domain.model.ExerciseRoutine
import com.example.domain.model.FitnessHistoryEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FitnessLocalManager(
    private val context: Context
) {

    private val gson = Gson()

    private val routinesFileName = "fitness_routines.json"
    private val historyFileName = "fitness_history.json"

    fun loadRoutines(): List<ExerciseRoutine> {
        return try {
            val json = context.openFileInput(routinesFileName)
                .bufferedReader()
                .use { it.readText() }

            val type = object : TypeToken<List<ExerciseRoutine>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun saveRoutines(routines: List<ExerciseRoutine>) {
        val json = gson.toJson(routines)
        context.openFileOutput(routinesFileName, Context.MODE_PRIVATE)
            .bufferedWriter()
            .use { it.write(json) }
    }

    fun loadHistory(): List<FitnessHistoryEntry> {
        return try {
            val json = context.openFileInput(historyFileName)
                .bufferedReader()
                .use { it.readText() }

            val type = object : TypeToken<List<FitnessHistoryEntry>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun saveHistory(history: List<FitnessHistoryEntry>) {
        val json = gson.toJson(history)
        context.openFileOutput(historyFileName, Context.MODE_PRIVATE)
            .bufferedWriter()
            .use { it.write(json) }
    }
}