package com.example.gymtracker.data.dao

import androidx.room.*
import com.example.gymtracker.data.entity.Food
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

    @Query("SELECT * FROM foods WHERE isArchived = 0 ORDER BY name ASC")
    fun getAllActiveFoods(): Flow<List<Food>>

    @Query("SELECT * FROM foods ORDER BY name ASC")
    fun getAllFoods(): Flow<List<Food>>

    @Query("SELECT * FROM foods WHERE id = :id")
    suspend fun getFoodById(id: Long): Food?

    @Query("SELECT * FROM foods WHERE name LIKE '%' || :query || '%' AND isArchived = 0 ORDER BY name ASC")
    fun searchFoods(query: String): Flow<List<Food>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(food: Food): Long

    @Update
    suspend fun update(food: Food)

    @Delete
    suspend fun delete(food: Food)

    @Query("UPDATE foods SET isArchived = 1 WHERE id = :id")
    suspend fun archive(id: Long)
}
