package com.example.gymtracker.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gymtracker.data.entity.Exercise
import com.example.gymtracker.data.entity.WorkoutPlan

data class WorkoutPlanWithExercises(
    @Embedded val plan: WorkoutPlan,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutPlanId"
    )
    val exercises: List<Exercise>
)
