package com.example.gymtracker.data.dao

import androidx.room.*
import com.example.gymtracker.data.entity.WorkoutSet
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutSetDao {

    @Query("SELECT * FROM workout_sets WHERE workoutSessionId = :sessionId ORDER BY orderIndex ASC, setNumber ASC")
    fun getSetsForSession(sessionId: Long): Flow<List<WorkoutSet>>

    @Query("SELECT * FROM workout_sets WHERE workoutSessionId = :sessionId ORDER BY orderIndex ASC, setNumber ASC")
    suspend fun getSetsForSessionOnce(sessionId: Long): List<WorkoutSet>

    @Query("SELECT SUM(weight * reps) FROM workout_sets WHERE workoutSessionId = :sessionId AND isCompleted = 1")
    fun getTotalVolumeForSession(sessionId: Long): Flow<Double?>

    @Query("SELECT SUM(weight * reps) FROM workout_sets WHERE workoutSessionId IN (SELECT id FROM workout_sessions WHERE date BETWEEN :startDate AND :endDate AND isCompleted = 1) AND isCompleted = 1")
    fun getTotalVolumeBetween(startDate: String, endDate: String): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(set: WorkoutSet): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sets: List<WorkoutSet>)

    @Update
    suspend fun update(set: WorkoutSet)

    @Delete
    suspend fun delete(set: WorkoutSet)

    @Query("DELETE FROM workout_sets WHERE workoutSessionId = :sessionId")
    suspend fun deleteAllForSession(sessionId: Long)
}
