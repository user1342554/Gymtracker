package com.example.gymtracker.data.repository

import com.example.gymtracker.data.dao.FoodDao
import com.example.gymtracker.data.dao.FoodLogEntryDao
import com.example.gymtracker.data.dao.NutritionGoalDao
import com.example.gymtracker.data.entity.Food
import com.example.gymtracker.data.entity.FoodLogEntry
import com.example.gymtracker.data.entity.NutritionGoal
import com.example.gymtracker.data.relation.DailyNutritionSummary
import kotlinx.coroutines.flow.Flow

class NutritionRepository(
    private val foodDao: FoodDao,
    private val foodLogEntryDao: FoodLogEntryDao,
    private val nutritionGoalDao: NutritionGoalDao
) {
    // Foods
    fun getAllActiveFoods(): Flow<List<Food>> = foodDao.getAllActiveFoods()
    fun getAllFoods(): Flow<List<Food>> = foodDao.getAllFoods()
    suspend fun getFoodById(id: Long): Food? = foodDao.getFoodById(id)
    fun searchFoods(query: String): Flow<List<Food>> = foodDao.searchFoods(query)
    suspend fun insertFood(food: Food): Long = foodDao.insert(food)
    suspend fun updateFood(food: Food) = foodDao.update(food)
    suspend fun deleteFood(food: Food) = foodDao.delete(food)
    suspend fun archiveFood(id: Long) = foodDao.archive(id)

    // Food Log
    fun getEntriesForDate(date: String): Flow<List<FoodLogEntry>> = foodLogEntryDao.getEntriesForDate(date)
    fun getDailySummary(date: String): Flow<DailyNutritionSummary?> = foodLogEntryDao.getDailySummary(date)
    fun getDailySummariesBetween(startDate: String, endDate: String): Flow<List<DailyNutritionSummary>> =
        foodLogEntryDao.getDailySummariesBetween(startDate, endDate)
    suspend fun insertFoodLogEntry(entry: FoodLogEntry): Long = foodLogEntryDao.insert(entry)
    suspend fun updateFoodLogEntry(entry: FoodLogEntry) = foodLogEntryDao.update(entry)
    suspend fun deleteFoodLogEntry(entry: FoodLogEntry) = foodLogEntryDao.delete(entry)
    suspend fun deleteFoodLogEntryById(id: Long) = foodLogEntryDao.deleteById(id)

    // Goals
    fun getNutritionGoal(): Flow<NutritionGoal?> = nutritionGoalDao.getGoal()
    suspend fun getNutritionGoalOnce(): NutritionGoal? = nutritionGoalDao.getGoalOnce()
    suspend fun saveNutritionGoal(goal: NutritionGoal) = nutritionGoalDao.insertOrUpdate(goal)
}
