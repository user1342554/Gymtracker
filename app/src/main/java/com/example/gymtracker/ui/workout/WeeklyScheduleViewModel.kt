package com.example.gymtracker.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.entity.WorkoutPlan
import com.example.gymtracker.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WeeklyScheduleViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    val assignmentsWithPlans = repository.getAllAssignmentsWithPlans()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allPlans = repository.getAllPlans()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun assignPlanToDay(dayOfWeek: Int, planId: Long) {
        viewModelScope.launch {
            repository.setAssignmentForDay(dayOfWeek, planId)
        }
    }

    fun clearDay(dayOfWeek: Int) {
        viewModelScope.launch {
            repository.clearAssignmentForDay(dayOfWeek)
        }
    }
}
