package com.example.gymtracker.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.relation.SessionWithSets
import com.example.gymtracker.data.repository.WorkoutRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class SessionDetailViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    private val _sessionId = MutableStateFlow(-1L)

    val sessionWithSets: StateFlow<SessionWithSets?> = _sessionId.flatMapLatest { id ->
        if (id > 0) repository.getSessionWithSets(id) else flowOf(null)
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun loadSession(sessionId: Long) {
        _sessionId.value = sessionId
    }
}
