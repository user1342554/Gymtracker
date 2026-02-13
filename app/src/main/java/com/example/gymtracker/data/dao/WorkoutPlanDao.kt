package com.example.gymtracker.data.dao

import androidx.room.*
import com.example.gymtracker.data.entity.WorkoutPlan
import com.example.gymtracker.data.relation.WorkoutPlanWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutPlanDao {

    @Query("SELECT * FROM workout_plans ORDER BY updatedAt DESC")
    fun getAllPlans(): Flow<List<WorkoutPlan>>

    @Query("SELECT * FROM workout_plans WHERE id = :id")
    fun getPlanById(id: Long): Flow<WorkoutPlan?>

    @Query("SELECT * FROM workout_plans WHERE id = :id")
    suspend fun getPlanByIdOnce(id: Long): WorkoutPlan?

    @Transaction
    @Query("SELECT * FROM workout_plans WHERE id = :id")
    fun getPlanWithExercises(id: Long): Flow<WorkoutPlanWithExercises?>

    @Transaction
    @Query("SELECT * FROM workout_plans ORDER BY updatedAt DESC")
    fun getAllPlansWithExercises(): Flow<List<WorkoutPlanWithExercises>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plan: WorkoutPlan): Long

    @Update
    suspend fun update(plan: WorkoutPlan)

    @Delete
    suspend fun delete(plan: WorkoutPlan)

    @Query("DELETE FROM workout_plans WHERE id = :id")
    suspend fun deleteById(id: Long)
}
