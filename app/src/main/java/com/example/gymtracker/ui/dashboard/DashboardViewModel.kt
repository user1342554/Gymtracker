package com.example.gymtracker.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.entity.BodyWeightEntry
import com.example.gymtracker.data.entity.WorkoutPlan
import com.example.gymtracker.data.relation.DailyNutritionSummary
import com.example.gymtracker.data.repository.BodyWeightRepository
import com.example.gymtracker.data.repository.NutritionRepository
import com.example.gymtracker.data.repository.WorkoutRepository
import com.example.gymtracker.util.DateUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DashboardState(
    val todayPlan: WorkoutPlan? = null,
    val todayNutrition: DailyNutritionSummary? = null,
    val latestWeight: BodyWeightEntry? = null,
    val todayWorkoutCompleted: Boolean = false
)

class DashboardViewModel(
    private val workoutRepository: WorkoutRepository,
    private val nutritionRepository: NutritionRepository,
    private val bodyWeightRepository: BodyWeightRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        val today = DateUtils.todayString()
        val currentDay = DateUtils.getCurrentDayOfWeek()

        viewModelScope.launch {
            // Load today's plan
            launch {
                val assignment = workoutRepository.getAssignmentWithPlanForDay(currentDay)
                _state.update { it.copy(todayPlan = assignment?.plan) }
            }

            // Check if today's workout is done
            launch {
                workoutRepository.getSessionsForDate(today).collect { sessions ->
                    _state.update { it.copy(todayWorkoutCompleted = sessions.isNotEmpty()) }
                }
            }

            // Load nutrition summary
            launch {
                nutritionRepository.getDailySummary(today).collect { summary ->
                    _state.update { it.copy(todayNutrition = summary) }
                }
            }

            // Load latest weight
            launch {
                bodyWeightRepository.getLatestEntry().collect { entry ->
                    _state.update { it.copy(latestWeight = entry) }
                }
            }
        }
    }
}
