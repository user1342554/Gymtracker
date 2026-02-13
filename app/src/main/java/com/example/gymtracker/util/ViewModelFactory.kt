package com.example.gymtracker.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gymtracker.data.GymDatabase
import com.example.gymtracker.data.repository.BodyWeightRepository
import com.example.gymtracker.data.repository.NutritionRepository
import com.example.gymtracker.data.repository.WorkoutRepository
import com.example.gymtracker.ui.dashboard.DashboardViewModel
import com.example.gymtracker.ui.nutrition.FoodManagerViewModel
import com.example.gymtracker.ui.nutrition.NutritionGoalsViewModel
import com.example.gymtracker.ui.nutrition.NutritionViewModel
import com.example.gymtracker.ui.stats.StatsViewModel
import com.example.gymtracker.ui.weight.BodyWeightViewModel
import com.example.gymtracker.ui.workout.ActiveWorkoutViewModel
import com.example.gymtracker.ui.workout.SessionDetailViewModel
import com.example.gymtracker.ui.workout.WeeklyScheduleViewModel
import com.example.gymtracker.ui.workout.WorkoutHistoryViewModel
import com.example.gymtracker.ui.workout.WorkoutPlanDetailViewModel
import com.example.gymtracker.ui.workout.WorkoutPlansViewModel

class ViewModelFactory(private val database: GymDatabase) : ViewModelProvider.Factory {

    private val workoutRepository by lazy {
        WorkoutRepository(
            database.workoutPlanDao(),
            database.exerciseDao(),
            database.weekdayPlanAssignmentDao(),
            database.workoutSessionDao(),
            database.workoutSetDao()
        )
    }

    private val nutritionRepository by lazy {
        NutritionRepository(
            database.foodDao(),
            database.foodLogEntryDao(),
            database.nutritionGoalDao()
        )
    }

    private val bodyWeightRepository by lazy {
        BodyWeightRepository(database.bodyWeightEntryDao())
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(BodyWeightViewModel::class.java) ->
                BodyWeightViewModel(bodyWeightRepository) as T
            modelClass.isAssignableFrom(WorkoutPlansViewModel::class.java) ->
                WorkoutPlansViewModel(workoutRepository) as T
            modelClass.isAssignableFrom(WorkoutPlanDetailViewModel::class.java) ->
                WorkoutPlanDetailViewModel(workoutRepository) as T
            modelClass.isAssignableFrom(WeeklyScheduleViewModel::class.java) ->
                WeeklyScheduleViewModel(workoutRepository) as T
            modelClass.isAssignableFrom(ActiveWorkoutViewModel::class.java) ->
                ActiveWorkoutViewModel(workoutRepository) as T
            modelClass.isAssignableFrom(WorkoutHistoryViewModel::class.java) ->
                WorkoutHistoryViewModel(workoutRepository) as T
            modelClass.isAssignableFrom(SessionDetailViewModel::class.java) ->
                SessionDetailViewModel(workoutRepository) as T
            modelClass.isAssignableFrom(NutritionViewModel::class.java) ->
                NutritionViewModel(nutritionRepository) as T
            modelClass.isAssignableFrom(FoodManagerViewModel::class.java) ->
                FoodManagerViewModel(nutritionRepository) as T
            modelClass.isAssignableFrom(NutritionGoalsViewModel::class.java) ->
                NutritionGoalsViewModel(nutritionRepository) as T
            modelClass.isAssignableFrom(DashboardViewModel::class.java) ->
                DashboardViewModel(workoutRepository, nutritionRepository, bodyWeightRepository) as T
            modelClass.isAssignableFrom(StatsViewModel::class.java) ->
                StatsViewModel(workoutRepository, nutritionRepository, bodyWeightRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
