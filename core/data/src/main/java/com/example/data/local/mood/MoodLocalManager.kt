package com.example.data.local.mood

import android.content.Context
import android.content.SharedPreferences
import com.example.domain.model.MoodEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.collections.toMutableList

class MoodLocalManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("zenith_mood_storage", Context.MODE_PRIVATE)

    private val gson = Gson()

    private val _moodEntriesFlow = MutableStateFlow(loadMoodEntries())
    val moodEntriesFlow: StateFlow<List<MoodEntry>> = _moodEntriesFlow.asStateFlow()

    companion object {
        private const val KEY_MOOD_ENTRIES = "mood_entries"
    }

    private fun loadMoodEntries(): List<MoodEntry> {
        val json = prefs.getString(KEY_MOOD_ENTRIES, null) ?: return emptyList()

        val type = object : TypeToken<List<MoodEntry>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun observeMoodEntries(): StateFlow<List<MoodEntry>> = moodEntriesFlow

    fun saveMoodEntry(entry: MoodEntry) {
        val currentList = loadMoodEntries().toMutableList()
        currentList.add(0, entry)

        val json = gson.toJson(currentList)

        prefs.edit()
            .putString(KEY_MOOD_ENTRIES, json)
            .apply()

        _moodEntriesFlow.value = currentList
    }
}