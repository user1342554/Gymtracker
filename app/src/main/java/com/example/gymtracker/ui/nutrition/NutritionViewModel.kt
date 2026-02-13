package com.example.gymtracker.ui.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.entity.Food
import com.example.gymtracker.data.entity.FoodLogEntry
import com.example.gymtracker.data.repository.NutritionRepository
import com.example.gymtracker.util.DateUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NutritionViewModel(
    private val repository: NutritionRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(DateUtils.todayString())
    val selectedDate: StateFlow<String> = _selectedDate

    @OptIn(ExperimentalCoroutinesApi::class)
    val foodLogEntries = _selectedDate.flatMapLatest { date ->
        repository.getEntriesForDate(date)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val dailySummary = _selectedDate.flatMapLatest { date ->
        repository.getDailySummary(date)
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val nutritionGoal = repository.getNutritionGoal()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val allFoods = repository.getAllActiveFoods()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setDate(date: String) {
        _selectedDate.value = date
    }

    fun deleteFoodLogEntry(entry: FoodLogEntry) {
        viewModelScope.launch {
            repository.deleteFoodLogEntry(entry)
        }
    }

    fun addFood(name: String, caloriesPer100g: Double, proteinPer100g: Double,
                carbsPer100g: Double, fatPer100g: Double) {
        viewModelScope.launch {
            repository.insertFood(
                Food(
                    name = name,
                    caloriesPerGram = caloriesPer100g / 100.0,
                    proteinPerGram = proteinPer100g / 100.0,
                    carbsPerGram = carbsPer100g / 100.0,
                    fatPerGram = fatPer100g / 100.0
                )
            )
        }
    }

    fun logFood(foodId: Long, foodName: String, grams: Double, caloriesPerGram: Double,
                proteinPerGram: Double, carbsPerGram: Double, fatPerGram: Double, mealType: String) {
        viewModelScope.launch {
            repository.insertFoodLogEntry(
                FoodLogEntry(
                    foodId = foodId,
                    foodNameSnapshot = foodName,
                    date = _selectedDate.value,
                    gramsEaten = grams,
                    caloriesSnapshot = caloriesPerGram * grams,
                    proteinSnapshot = proteinPerGram * grams,
                    carbsSnapshot = carbsPerGram * grams,
                    fatSnapshot = fatPerGram * grams,
                    mealType = mealType
                )
            )
        }
    }
}
