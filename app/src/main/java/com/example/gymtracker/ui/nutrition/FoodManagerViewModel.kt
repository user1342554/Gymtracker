package com.example.gymtracker.ui.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.entity.Food
import com.example.gymtracker.data.repository.NutritionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class FoodManagerViewModel(
    private val repository: NutritionRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    val foods: StateFlow<List<Food>> = _searchQuery.flatMapLatest { query ->
        if (query.isBlank()) repository.getAllActiveFoods()
        else repository.searchFoods(query)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
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

    fun updateFood(food: Food, name: String, caloriesPer100g: Double, proteinPer100g: Double,
                   carbsPer100g: Double, fatPer100g: Double) {
        viewModelScope.launch {
            repository.updateFood(
                food.copy(
                    name = name,
                    caloriesPerGram = caloriesPer100g / 100.0,
                    proteinPerGram = proteinPer100g / 100.0,
                    carbsPerGram = carbsPer100g / 100.0,
                    fatPerGram = fatPer100g / 100.0
                )
            )
        }
    }

    fun deleteFood(food: Food) {
        viewModelScope.launch {
            repository.archiveFood(food.id)
        }
    }
}
