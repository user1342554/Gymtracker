package com.example.gymtracker.data.dao

import androidx.room.*
import com.example.gymtracker.data.entity.BodyWeightEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface BodyWeightEntryDao {

    @Query("SELECT * FROM body_weight_entries ORDER BY date DESC")
    fun getAllEntries(): Flow<List<BodyWeightEntry>>

    @Query("SELECT * FROM body_weight_entries ORDER BY date DESC LIMIT 1")
    fun getLatestEntry(): Flow<BodyWeightEntry?>

    @Query("SELECT * FROM body_weight_entries WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getEntriesBetween(startDate: String, endDate: String): Flow<List<BodyWeightEntry>>

    @Query("SELECT * FROM body_weight_entries WHERE date = :date")
    suspend fun getEntryForDate(date: String): BodyWeightEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: BodyWeightEntry): Long

    @Update
    suspend fun update(entry: BodyWeightEntry)

    @Delete
    suspend fun delete(entry: BodyWeightEntry)

    @Query("DELETE FROM body_weight_entries WHERE id = :id")
    suspend fun deleteById(id: Long)
}
