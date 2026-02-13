package com.example.gymtracker.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "body_weight_entries",
    indices = [Index(value = ["date"], unique = true)]
)
data class BodyWeightEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String, // yyyy-MM-dd
    val weightKg: Double,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
