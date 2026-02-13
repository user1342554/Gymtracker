package com.example.gymtracker.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.entity.Exercise
import com.example.gymtracker.data.entity.WorkoutSession
import com.example.gymtracker.data.entity.WorkoutSet
import com.example.gymtracker.data.repository.WorkoutRepository
import com.example.gymtracker.util.DateUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ActiveExercise(
    val exercise: Exercise,
    val sets: MutableList<ActiveSet>
)

data class ActiveSet(
    val setNumber: Int,
    var weight: Double = 0.0,
    var reps: Int = 0,
    var isCompleted: Boolean = false
)

class ActiveWorkoutViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    private val _sessionId = MutableStateFlow(-1L)
    val sessionId: StateFlow<Long> = _sessionId

    private val _exercises = MutableStateFlow<List<ActiveExercise>>(emptyList())
    val exercises: StateFlow<List<ActiveExercise>> = _exercises

    private val _planName = MutableStateFlow("")
    val planName: StateFlow<String> = _planName

    private val _restTimerSeconds = MutableStateFlow(0)
    val restTimerSeconds: StateFlow<Int> = _restTimerSeconds

    private val _isRestTimerRunning = MutableStateFlow(false)
    val isRestTimerRunning: StateFlow<Boolean> = _isRestTimerRunning

    private var restTimerJob: Job? = null
    private var currentRestDuration = 60

    fun startWorkout(planId: Long) {
        if (_sessionId.value > 0) return // already started
        viewModelScope.launch {
            val plan = if (planId > 0) repository.getPlanByIdOnce(planId) else null
            val planName = plan?.name ?: "Quick Workout"
            _planName.value = planName

            val session = WorkoutSession(
                workoutPlanId = if (planId > 0) planId else null,
                planNameSnapshot = planName,
                date = DateUtils.todayString(),
                startTime = System.currentTimeMillis()
            )
            val sessionId = repository.insertSession(session)
            _sessionId.value = sessionId

            // Load exercises from plan
            if (planId > 0) {
                val exercises = repository.getExercisesForPlanOnce(planId)
                _exercises.value = exercises.map { exercise ->
                    ActiveExercise(
                        exercise = exercise,
                        sets = (1..exercise.targetSets).map { setNum ->
                            ActiveSet(setNumber = setNum)
                        }.toMutableList()
                    )
                }
            }
        }
    }

    fun updateSet(exerciseIndex: Int, setIndex: Int, weight: Double, reps: Int) {
        val current = _exercises.value.toMutableList()
        if (exerciseIndex < current.size) {
            val exercise = current[exerciseIndex]
            if (setIndex < exercise.sets.size) {
                exercise.sets[setIndex] = exercise.sets[setIndex].copy(
                    weight = weight,
                    reps = reps
                )
                _exercises.value = current.toList()
            }
        }
    }

    fun completeSet(exerciseIndex: Int, setIndex: Int) {
        val current = _exercises.value.toMutableList()
        if (exerciseIndex < current.size) {
            val exercise = current[exerciseIndex]
            if (setIndex < exercise.sets.size) {
                val set = exercise.sets[setIndex]
                exercise.sets[setIndex] = set.copy(isCompleted = !set.isCompleted)
                _exercises.value = current.toList()

                // Start rest timer when completing a set
                if (!set.isCompleted) {
                    currentRestDuration = exercise.exercise.restSeconds
                    startRestTimer(currentRestDuration)
                }
            }
        }
    }

    fun addSet(exerciseIndex: Int) {
        val current = _exercises.value.toMutableList()
        if (exerciseIndex < current.size) {
            val exercise = current[exerciseIndex]
            val nextSetNum = exercise.sets.size + 1
            exercise.sets.add(ActiveSet(setNumber = nextSetNum))
            _exercises.value = current.toList()
        }
    }

    fun startRestTimer(seconds: Int) {
        restTimerJob?.cancel()
        _restTimerSeconds.value = seconds
        _isRestTimerRunning.value = true
        restTimerJob = viewModelScope.launch {
            while (_restTimerSeconds.value > 0) {
                delay(1000)
                _restTimerSeconds.value -= 1
            }
            _isRestTimerRunning.value = false
        }
    }

    fun stopRestTimer() {
        restTimerJob?.cancel()
        _isRestTimerRunning.value = false
        _restTimerSeconds.value = 0
    }

    fun finishWorkout(onComplete: () -> Unit) {
        val sessionId = _sessionId.value
        if (sessionId <= 0) return

        viewModelScope.launch {
            // Save all sets
            var orderIndex = 0
            _exercises.value.forEach { activeExercise ->
                activeExercise.sets.forEach { activeSet ->
                    repository.insertSet(
                        WorkoutSet(
                            workoutSessionId = sessionId,
                            exerciseName = activeExercise.exercise.name,
                            exerciseId = activeExercise.exercise.id,
                            setNumber = activeSet.setNumber,
                            weight = activeSet.weight,
                            reps = activeSet.reps,
                            isCompleted = activeSet.isCompleted,
                            orderIndex = orderIndex++
                        )
                    )
                }
            }

            // Update session as completed
            val session = repository.getSessionByIdOnce(sessionId)
            if (session != null) {
                repository.updateSession(
                    session.copy(
                        endTime = System.currentTimeMillis(),
                        isCompleted = true
                    )
                )
            }

            stopRestTimer()
            onComplete()
        }
    }

    fun discardWorkout(onComplete: () -> Unit) {
        val sessionId = _sessionId.value
        if (sessionId <= 0) {
            onComplete()
            return
        }
        viewModelScope.launch {
            repository.deleteSessionById(sessionId)
            stopRestTimer()
            onComplete()
        }
    }
}
