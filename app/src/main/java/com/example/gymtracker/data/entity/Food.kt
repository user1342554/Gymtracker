package com.example.gymtracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class Food(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val caloriesPerGram: Double, // kcal per gram
    val proteinPerGram: Double,
    val carbsPerGram: Double,
    val fatPerGram: Double,
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
