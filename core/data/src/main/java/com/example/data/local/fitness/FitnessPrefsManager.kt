package com.example.data.local.fitness

import android.content.Context

class FitnessPrefsManager(context: Context) {

    private val prefs =
        context.getSharedPreferences("zenith_fitness_prefs", Context.MODE_PRIVATE)

    fun saveLastWorkoutDate(date: String) {
        prefs.edit().putString("last_workout_date", date).apply()
    }

    fun getLastWorkoutDate(): String? {
        return prefs.getString("last_workout_date", null)
    }

    fun saveUsualWorkoutHour(hour: Int) {
        prefs.edit().putInt("usual_workout_hour", hour).apply()
    }

    fun getUsualWorkoutHour(): Int {
        return prefs.getInt("usual_workout_hour", 18)
    }

    fun saveUsualWorkoutMinute(minute: Int) {
        prefs.edit().putInt("usual_workout_minute", minute).apply()
    }

    fun getUsualWorkoutMinute(): Int {
        return prefs.getInt("usual_workout_minute", 0)
    }
}