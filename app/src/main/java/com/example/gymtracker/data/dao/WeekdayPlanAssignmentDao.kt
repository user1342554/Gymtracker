package com.example.gymtracker.data.dao

import androidx.room.*
import com.example.gymtracker.data.entity.WeekdayPlanAssignment
import com.example.gymtracker.data.relation.WeekdayWithPlan
import kotlinx.coroutines.flow.Flow

@Dao
interface WeekdayPlanAssignmentDao {

    @Transaction
    @Query("SELECT * FROM weekday_plan_assignments ORDER BY dayOfWeek ASC")
    fun getAllAssignmentsWithPlans(): Flow<List<WeekdayWithPlan>>

    @Query("SELECT * FROM weekday_plan_assignments WHERE dayOfWeek = :dayOfWeek")
    suspend fun getAssignmentForDay(dayOfWeek: Int): WeekdayPlanAssignment?

    @Transaction
    @Query("SELECT * FROM weekday_plan_assignments WHERE dayOfWeek = :dayOfWeek")
    suspend fun getAssignmentWithPlanForDay(dayOfWeek: Int): WeekdayWithPlan?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(assignment: WeekdayPlanAssignment)

    @Query("DELETE FROM weekday_plan_assignments WHERE dayOfWeek = :dayOfWeek")
    suspend fun deleteForDay(dayOfWeek: Int)

    @Query("DELETE FROM weekday_plan_assignments")
    suspend fun deleteAll()
}
