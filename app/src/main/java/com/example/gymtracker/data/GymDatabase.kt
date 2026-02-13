package com.example.gymtracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gymtracker.data.dao.*
import com.example.gymtracker.data.entity.*

@Database(
    entities = [
        WorkoutPlan::class,
        Exercise::class,
        WeekdayPlanAssignment::class,
        WorkoutSession::class,
        WorkoutSet::class,
        Food::class,
        FoodLogEntry::class,
        NutritionGoal::class,
        BodyWeightEntry::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GymDatabase : RoomDatabase() {

    abstract fun workoutPlanDao(): WorkoutPlanDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun weekdayPlanAssignmentDao(): WeekdayPlanAssignmentDao
    abstract fun workoutSessionDao(): WorkoutSessionDao
    abstract fun workoutSetDao(): WorkoutSetDao
    abstract fun foodDao(): FoodDao
    abstract fun foodLogEntryDao(): FoodLogEntryDao
    abstract fun nutritionGoalDao(): NutritionGoalDao
    abstract fun bodyWeightEntryDao(): BodyWeightEntryDao

    companion object {
        @Volatile
        private var INSTANCE: GymDatabase? = null

        fun getInstance(context: Context): GymDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GymDatabase::class.java,
                    "gym_tracker_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
