package com.example.gymtracker.data.relation

data class DailyNutritionSummary(
    val date: String,
    val totalCalories: Double,
    val totalProtein: Double,
    val totalCarbs: Double,
    val totalFat: Double
)
