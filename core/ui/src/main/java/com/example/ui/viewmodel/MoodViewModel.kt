package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.MoodEntry
import com.example.domain.model.MoodMosaicItem
import com.example.domain.usecase.mood.ObserveMoodEntriesUseCase
import com.example.domain.usecase.mood.SaveMoodEntryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.min

class MoodViewModel(
    private val observeMoodEntriesUseCase: ObserveMoodEntriesUseCase,
    private val saveMoodEntryUseCase: SaveMoodEntryUseCase
) : ViewModel() {

    private val _moodEntries = MutableStateFlow<List<MoodEntry>>(emptyList())
    val moodEntries: StateFlow<List<MoodEntry>> = _moodEntries.asStateFlow()

    init {
        observeEntries()
    }

    private fun observeEntries() {
        viewModelScope.launch {
            observeMoodEntriesUseCase().collect { entries ->
                _moodEntries.value = entries
            }
        }
    }

    fun saveMood(
        moodId: String,
        moodLabel: String,
        note: String
    ) {
        viewModelScope.launch {
            saveMoodEntryUseCase(
                MoodEntry(
                    moodId = moodId,
                    moodLabel = moodLabel,
                    note = note
                )
            )
        }
    }

    fun getMosaicForPeriod(period: String): List<MoodMosaicItem> {
        val now = System.currentTimeMillis()

        val filtered = _moodEntries.value.filter { entry ->
            when (period) {
                "day" -> now - entry.timestamp <= 1L * 24 * 60 * 60 * 1000
                "week" -> now - entry.timestamp <= 7L * 24 * 60 * 60 * 1000
                "month" -> now - entry.timestamp <= 30L * 24 * 60 * 60 * 1000
                else -> true
            }
        }

        return filtered
            .groupBy { it.moodId }
            .map { (_, entries) ->
                MoodMosaicItem(
                    moodId = entries.first().moodId,
                    moodLabel = entries.first().moodLabel,
                    weight = min(entries.size, 10)
                )
            }
            .shuffled()
    }
}