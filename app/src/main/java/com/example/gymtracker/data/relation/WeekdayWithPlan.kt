package com.example.gymtracker.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gymtracker.data.entity.WeekdayPlanAssignment
import com.example.gymtracker.data.entity.WorkoutPlan

data class WeekdayWithPlan(
    @Embedded val assignment: WeekdayPlanAssignment,
    @Relation(
        parentColumn = "workoutPlanId",
        entityColumn = "id"
    )
    val plan: WorkoutPlan?
)
