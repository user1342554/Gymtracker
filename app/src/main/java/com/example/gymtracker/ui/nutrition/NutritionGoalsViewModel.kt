package com.example.gymtracker.ui.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.entity.NutritionGoal
import com.example.gymtracker.data.repository.NutritionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NutritionGoalsViewModel(
    private val repository: NutritionRepository
) : ViewModel() {

    val goal = repository.getNutritionGoal()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun saveGoal(calories: Int, protein: Int, carbs: Int, fat: Int) {
        viewModelScope.launch {
            repository.saveNutritionGoal(
                NutritionGoal(
                    dailyCalories = calories,
                    dailyProtein = protein,
                    dailyCarbs = carbs,
                    dailyFat = fat
                )
            )
        }
    }
}
