package com.example.gymtracker.ui.weight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.entity.BodyWeightEntry
import com.example.gymtracker.data.repository.BodyWeightRepository
import com.example.gymtracker.util.DateUtils
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BodyWeightViewModel(
    private val repository: BodyWeightRepository
) : ViewModel() {

    val allEntries = repository.getAllEntries()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val latestEntry = repository.getLatestEntry()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun logWeight(weightKg: Double, notes: String = "", date: String = DateUtils.todayString()) {
        viewModelScope.launch {
            val existing = repository.getEntryForDate(date)
            if (existing != null) {
                repository.updateEntry(existing.copy(weightKg = weightKg, notes = notes))
            } else {
                repository.insertEntry(
                    BodyWeightEntry(date = date, weightKg = weightKg, notes = notes)
                )
            }
        }
    }

    fun deleteEntry(entry: BodyWeightEntry) {
        viewModelScope.launch {
            repository.deleteEntry(entry)
        }
    }
}
