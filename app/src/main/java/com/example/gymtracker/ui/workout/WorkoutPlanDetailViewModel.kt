package com.example.gymtracker.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.entity.Exercise
import com.example.gymtracker.data.entity.WorkoutPlan
import com.example.gymtracker.data.repository.WorkoutRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class WorkoutPlanDetailViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    private val _planId = MutableStateFlow(-1L)

    val plan: StateFlow<WorkoutPlan?> = _planId.flatMapLatest { id ->
        if (id > 0) repository.getPlanById(id) else flowOf(null)
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val exercises: StateFlow<List<Exercise>> = _planId.flatMapLatest { id ->
        if (id > 0) repository.getExercisesForPlan(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun loadPlan(planId: Long) {
        _planId.value = planId
    }

    fun addExercise(name: String, targetSets: Int, targetReps: Int, restSeconds: Int, notes: String) {
        val planId = _planId.value
        if (planId <= 0) return
        viewModelScope.launch {
            val orderIndex = repository.getNextExerciseOrderIndex(planId)
            repository.insertExercise(
                Exercise(
                    workoutPlanId = planId,
                    name = name,
                    targetSets = targetSets,
                    targetReps = targetReps,
                    restSeconds = restSeconds,
                    orderIndex = orderIndex,
                    notes = notes
                )
            )
        }
    }

    fun updateExercise(exercise: Exercise) {
        viewModelScope.launch {
            repository.updateExercise(exercise)
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            repository.deleteExercise(exercise)
        }
    }
}
