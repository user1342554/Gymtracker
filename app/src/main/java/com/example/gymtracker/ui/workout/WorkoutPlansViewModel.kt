package com.example.gymtracker.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.entity.WorkoutPlan
import com.example.gymtracker.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WorkoutPlansViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    val allPlansWithExercises = repository.getAllPlansWithExercises()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addPlan(name: String, description: String, colorHex: String) {
        viewModelScope.launch {
            repository.insertPlan(
                WorkoutPlan(name = name, description = description, colorHex = colorHex)
            )
        }
    }

    fun updatePlan(plan: WorkoutPlan) {
        viewModelScope.launch {
            repository.updatePlan(plan.copy(updatedAt = System.currentTimeMillis()))
        }
    }

    fun deletePlan(plan: WorkoutPlan) {
        viewModelScope.launch {
            repository.deletePlan(plan)
        }
    }
}
