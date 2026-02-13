package com.example.gymtracker.data.repository

import com.example.gymtracker.data.dao.*
import com.example.gymtracker.data.entity.*
import com.example.gymtracker.data.relation.SessionWithSets
import com.example.gymtracker.data.relation.WeekdayWithPlan
import com.example.gymtracker.data.relation.WorkoutPlanWithExercises
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(
    private val workoutPlanDao: WorkoutPlanDao,
    private val exerciseDao: ExerciseDao,
    private val weekdayPlanAssignmentDao: WeekdayPlanAssignmentDao,
    private val workoutSessionDao: WorkoutSessionDao,
    private val workoutSetDao: WorkoutSetDao
) {
    // Plans
    fun getAllPlans(): Flow<List<WorkoutPlan>> = workoutPlanDao.getAllPlans()
    fun getPlanById(id: Long): Flow<WorkoutPlan?> = workoutPlanDao.getPlanById(id)
    suspend fun getPlanByIdOnce(id: Long): WorkoutPlan? = workoutPlanDao.getPlanByIdOnce(id)
    fun getPlanWithExercises(id: Long): Flow<WorkoutPlanWithExercises?> = workoutPlanDao.getPlanWithExercises(id)
    fun getAllPlansWithExercises(): Flow<List<WorkoutPlanWithExercises>> = workoutPlanDao.getAllPlansWithExercises()
    suspend fun insertPlan(plan: WorkoutPlan): Long = workoutPlanDao.insert(plan)
    suspend fun updatePlan(plan: WorkoutPlan) = workoutPlanDao.update(plan)
    suspend fun deletePlan(plan: WorkoutPlan) = workoutPlanDao.delete(plan)
    suspend fun deletePlanById(id: Long) = workoutPlanDao.deleteById(id)

    // Exercises
    fun getExercisesForPlan(planId: Long): Flow<List<Exercise>> = exerciseDao.getExercisesForPlan(planId)
    suspend fun getExercisesForPlanOnce(planId: Long): List<Exercise> = exerciseDao.getExercisesForPlanOnce(planId)
    suspend fun insertExercise(exercise: Exercise): Long = exerciseDao.insert(exercise)
    suspend fun updateExercise(exercise: Exercise) = exerciseDao.update(exercise)
    suspend fun deleteExercise(exercise: Exercise) = exerciseDao.delete(exercise)
    suspend fun deleteExerciseById(id: Long) = exerciseDao.deleteById(id)
    suspend fun getNextExerciseOrderIndex(planId: Long): Int = exerciseDao.getNextOrderIndex(planId)

    // Weekly Schedule
    fun getAllAssignmentsWithPlans(): Flow<List<WeekdayWithPlan>> = weekdayPlanAssignmentDao.getAllAssignmentsWithPlans()
    suspend fun getAssignmentForDay(dayOfWeek: Int): WeekdayPlanAssignment? = weekdayPlanAssignmentDao.getAssignmentForDay(dayOfWeek)
    suspend fun getAssignmentWithPlanForDay(dayOfWeek: Int): WeekdayWithPlan? = weekdayPlanAssignmentDao.getAssignmentWithPlanForDay(dayOfWeek)
    suspend fun setAssignmentForDay(dayOfWeek: Int, planId: Long) {
        weekdayPlanAssignmentDao.insert(WeekdayPlanAssignment(dayOfWeek = dayOfWeek, workoutPlanId = planId))
    }
    suspend fun clearAssignmentForDay(dayOfWeek: Int) = weekdayPlanAssignmentDao.deleteForDay(dayOfWeek)

    // Sessions
    fun getAllSessions(): Flow<List<WorkoutSession>> = workoutSessionDao.getAllSessions()
    fun getCompletedSessions(): Flow<List<WorkoutSession>> = workoutSessionDao.getCompletedSessions()
    fun getSessionById(id: Long): Flow<WorkoutSession?> = workoutSessionDao.getSessionById(id)
    suspend fun getSessionByIdOnce(id: Long): WorkoutSession? = workoutSessionDao.getSessionByIdOnce(id)
    fun getSessionWithSets(id: Long): Flow<SessionWithSets?> = workoutSessionDao.getSessionWithSets(id)
    fun getSessionsForDate(date: String): Flow<List<WorkoutSession>> = workoutSessionDao.getSessionsForDate(date)
    fun getSessionsBetween(startDate: String, endDate: String): Flow<List<WorkoutSession>> = workoutSessionDao.getSessionsBetween(startDate, endDate)
    fun getCompletedCountBetween(startDate: String, endDate: String): Flow<Int> = workoutSessionDao.getCompletedCountBetween(startDate, endDate)
    suspend fun insertSession(session: WorkoutSession): Long = workoutSessionDao.insert(session)
    suspend fun updateSession(session: WorkoutSession) = workoutSessionDao.update(session)
    suspend fun deleteSession(session: WorkoutSession) = workoutSessionDao.delete(session)
    suspend fun deleteSessionById(id: Long) = workoutSessionDao.deleteById(id)

    // Sets
    fun getSetsForSession(sessionId: Long): Flow<List<WorkoutSet>> = workoutSetDao.getSetsForSession(sessionId)
    suspend fun getSetsForSessionOnce(sessionId: Long): List<WorkoutSet> = workoutSetDao.getSetsForSessionOnce(sessionId)
    fun getTotalVolumeForSession(sessionId: Long): Flow<Double?> = workoutSetDao.getTotalVolumeForSession(sessionId)
    fun getTotalVolumeBetween(startDate: String, endDate: String): Flow<Double?> = workoutSetDao.getTotalVolumeBetween(startDate, endDate)
    suspend fun insertSet(set: WorkoutSet): Long = workoutSetDao.insert(set)
    suspend fun insertSets(sets: List<WorkoutSet>) = workoutSetDao.insertAll(sets)
    suspend fun updateSet(set: WorkoutSet) = workoutSetDao.update(set)
    suspend fun deleteSet(set: WorkoutSet) = workoutSetDao.delete(set)
}
