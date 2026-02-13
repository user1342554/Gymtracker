package com.example.gymtracker.data.dao

import androidx.room.*
import com.example.gymtracker.data.entity.NutritionGoal
import kotlinx.coroutines.flow.Flow

@Dao
interface NutritionGoalDao {

    @Query("SELECT * FROM nutrition_goals WHERE id = 1")
    fun getGoal(): Flow<NutritionGoal?>

    @Query("SELECT * FROM nutrition_goals WHERE id = 1")
    suspend fun getGoalOnce(): NutritionGoal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(goal: NutritionGoal)
}
