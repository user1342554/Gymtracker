package com.example.gymtracker.data.dao

import androidx.room.*
import com.example.gymtracker.data.entity.FoodLogEntry
import com.example.gymtracker.data.relation.DailyNutritionSummary
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodLogEntryDao {

    @Query("SELECT * FROM food_log_entries WHERE date = :date ORDER BY mealType ASC, createdAt ASC")
    fun getEntriesForDate(date: String): Flow<List<FoodLogEntry>>

    @Query("""
        SELECT date,
               SUM(caloriesSnapshot) as totalCalories,
               SUM(proteinSnapshot) as totalProtein,
               SUM(carbsSnapshot) as totalCarbs,
               SUM(fatSnapshot) as totalFat
        FROM food_log_entries
        WHERE date = :date
        GROUP BY date
    """)
    fun getDailySummary(date: String): Flow<DailyNutritionSummary?>

    @Query("""
        SELECT date,
               SUM(caloriesSnapshot) as totalCalories,
               SUM(proteinSnapshot) as totalProtein,
               SUM(carbsSnapshot) as totalCarbs,
               SUM(fatSnapshot) as totalFat
        FROM food_log_entries
        WHERE date BETWEEN :startDate AND :endDate
        GROUP BY date
        ORDER BY date ASC
    """)
    fun getDailySummariesBetween(startDate: String, endDate: String): Flow<List<DailyNutritionSummary>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: FoodLogEntry): Long

    @Update
    suspend fun update(entry: FoodLogEntry)

    @Delete
    suspend fun delete(entry: FoodLogEntry)

    @Query("DELETE FROM food_log_entries WHERE id = :id")
    suspend fun deleteById(id: Long)
}
