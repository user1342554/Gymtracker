package com.example.gymtracker.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.entity.BodyWeightEntry
import com.example.gymtracker.data.entity.WorkoutSession
import com.example.gymtracker.data.relation.DailyNutritionSummary
import com.example.gymtracker.data.repository.BodyWeightRepository
import com.example.gymtracker.data.repository.NutritionRepository
import com.example.gymtracker.data.repository.WorkoutRepository
import com.example.gymtracker.util.DateUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class StatsViewModel(
    private val workoutRepository: WorkoutRepository,
    private val nutritionRepository: NutritionRepository,
    private val bodyWeightRepository: BodyWeightRepository
) : ViewModel() {

    private val _periodDays = MutableStateFlow(30)
    val periodDays: StateFlow<Int> = _periodDays

    val workoutSessions: StateFlow<List<WorkoutSession>> = _periodDays.flatMapLatest { days ->
        val start = DateUtils.daysAgo(days)
        val end = DateUtils.todayString()
        workoutRepository.getSessionsBetween(start, end)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val workoutCount: StateFlow<Int> = _periodDays.flatMapLatest { days ->
        val start = DateUtils.daysAgo(days)
        val end = DateUtils.todayString()
        workoutRepository.getCompletedCountBetween(start, end)
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val totalVolume: StateFlow<Double?> = _periodDays.flatMapLatest { days ->
        val start = DateUtils.daysAgo(days)
        val end = DateUtils.todayString()
        workoutRepository.getTotalVolumeBetween(start, end)
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val nutritionSummaries: StateFlow<List<DailyNutritionSummary>> = _periodDays.flatMapLatest { days ->
        val start = DateUtils.daysAgo(days)
        val end = DateUtils.todayString()
        nutritionRepository.getDailySummariesBetween(start, end)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val weightEntries: StateFlow<List<BodyWeightEntry>> = _periodDays.flatMapLatest { days ->
        val start = DateUtils.daysAgo(days)
        val end = DateUtils.todayString()
        bodyWeightRepository.getEntriesBetween(start, end)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setPeriod(days: Int) {
        _periodDays.value = days
    }
}
