package com.example.gymtracker.data.dao

import androidx.room.*
import com.example.gymtracker.data.entity.WorkoutSession
import com.example.gymtracker.data.relation.SessionWithSets
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutSessionDao {

    @Query("SELECT * FROM workout_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<WorkoutSession>>

    @Query("SELECT * FROM workout_sessions WHERE isCompleted = 1 ORDER BY startTime DESC")
    fun getCompletedSessions(): Flow<List<WorkoutSession>>

    @Query("SELECT * FROM workout_sessions WHERE id = :id")
    fun getSessionById(id: Long): Flow<WorkoutSession?>

    @Query("SELECT * FROM workout_sessions WHERE id = :id")
    suspend fun getSessionByIdOnce(id: Long): WorkoutSession?

    @Transaction
    @Query("SELECT * FROM workout_sessions WHERE id = :id")
    fun getSessionWithSets(id: Long): Flow<SessionWithSets?>

    @Query("SELECT * FROM workout_sessions WHERE date = :date AND isCompleted = 1")
    fun getSessionsForDate(date: String): Flow<List<WorkoutSession>>

    @Query("SELECT * FROM workout_sessions WHERE date BETWEEN :startDate AND :endDate AND isCompleted = 1 ORDER BY startTime DESC")
    fun getSessionsBetween(startDate: String, endDate: String): Flow<List<WorkoutSession>>

    @Query("SELECT COUNT(*) FROM workout_sessions WHERE date BETWEEN :startDate AND :endDate AND isCompleted = 1")
    fun getCompletedCountBetween(startDate: String, endDate: String): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: WorkoutSession): Long

    @Update
    suspend fun update(session: WorkoutSession)

    @Delete
    suspend fun delete(session: WorkoutSession)

    @Query("DELETE FROM workout_sessions WHERE id = :id")
    suspend fun deleteById(id: Long)
}
