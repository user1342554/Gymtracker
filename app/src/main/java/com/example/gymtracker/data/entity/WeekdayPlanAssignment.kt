package com.example.gymtracker.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "weekday_plan_assignments",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutPlan::class,
            parentColumns = ["id"],
            childColumns = ["workoutPlanId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("workoutPlanId"), Index(value = ["dayOfWeek"], unique = true)]
)
data class WeekdayPlanAssignment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dayOfWeek: Int, // 1=Monday .. 7=Sunday (ISO)
    val workoutPlanId: Long
)
