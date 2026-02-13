package com.example.gymtracker.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "food_log_entries",
    foreignKeys = [
        ForeignKey(
            entity = Food::class,
            parentColumns = ["id"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("foodId"), Index("date")]
)
data class FoodLogEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val foodId: Long? = null,
    val foodNameSnapshot: String,
    val date: String, // yyyy-MM-dd
    val gramsEaten: Double,
    val caloriesSnapshot: Double, // total calories for this entry
    val proteinSnapshot: Double,
    val carbsSnapshot: Double,
    val fatSnapshot: Double,
    val mealType: String = "Snack", // Breakfast, Lunch, Dinner, Snack
    val createdAt: Long = System.currentTimeMillis()
)
