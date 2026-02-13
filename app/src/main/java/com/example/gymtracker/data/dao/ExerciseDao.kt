package com.example.gymtracker.data.dao

import androidx.room.*
import com.example.gymtracker.data.entity.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercises WHERE workoutPlanId = :planId ORDER BY orderIndex ASC")
    fun getExercisesForPlan(planId: Long): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE workoutPlanId = :planId ORDER BY orderIndex ASC")
    suspend fun getExercisesForPlanOnce(planId: Long): List<Exercise>

    @Query("SELECT * FROM exercises WHERE id = :id")
    suspend fun getExerciseById(id: Long): Exercise?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: Exercise): Long

    @Update
    suspend fun update(exercise: Exercise)

    @Delete
    suspend fun delete(exercise: Exercise)

    @Query("DELETE FROM exercises WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COALESCE(MAX(orderIndex), -1) + 1 FROM exercises WHERE workoutPlanId = :planId")
    suspend fun getNextOrderIndex(planId: Long): Int
}
