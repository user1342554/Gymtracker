package com.example.gymtracker.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.gymtracker.data.entity.WorkoutSession
import com.example.gymtracker.data.entity.WorkoutSet

data class SessionWithSets(
    @Embedded val session: WorkoutSession,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutSessionId"
    )
    val sets: List<WorkoutSet>
)
