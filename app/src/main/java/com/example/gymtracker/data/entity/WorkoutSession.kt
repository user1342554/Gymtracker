package com.example.gymtracker.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workout_sessions",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutPlan::class,
            parentColumns = ["id"],
            childColumns = ["workoutPlanId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("workoutPlanId"), Index("date")]
)
data class WorkoutSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val workoutPlanId: Long? = null,
    val planNameSnapshot: String = "",
    val date: String, // yyyy-MM-dd
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long? = null,
    val notes: String = "",
    val isCompleted: Boolean = false
)
