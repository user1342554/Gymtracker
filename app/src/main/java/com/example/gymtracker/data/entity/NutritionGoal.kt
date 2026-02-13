package com.example.gymtracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nutrition_goals")
data class NutritionGoal(
    @PrimaryKey
    val id: Int = 1, // single row
    val dailyCalories: Int = 2000,
    val dailyProtein: Int = 150,
    val dailyCarbs: Int = 250,
    val dailyFat: Int = 65
)
