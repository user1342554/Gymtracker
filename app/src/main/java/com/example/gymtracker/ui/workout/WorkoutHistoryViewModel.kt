package com.example.gymtracker.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.entity.WorkoutSession
import com.example.gymtracker.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WorkoutHistoryViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    val completedSessions = repository.getCompletedSessions()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun deleteSession(session: WorkoutSession) {
        viewModelScope.launch {
            repository.deleteSession(session)
        }
    }
}
