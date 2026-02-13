package com.example.gymtracker.ui.workout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtracker.databinding.ItemActiveExerciseBinding

class ActiveExerciseAdapter(
    private val onSetCompleted: (exerciseIndex: Int, setIndex: Int) -> Unit,
    private val onSetUpdated: (exerciseIndex: Int, setIndex: Int, weight: Double, reps: Int) -> Unit,
    private val onAddSet: (exerciseIndex: Int) -> Unit
) : RecyclerView.Adapter<ActiveExerciseAdapter.ViewHolder>() {

    private val exercises = mutableListOf<ActiveExercise>()

    fun submitList(list: List<ActiveExercise>) {
        exercises.clear()
        exercises.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemActiveExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(activeExercise: ActiveExercise, exerciseIndex: Int) {
            binding.tvExerciseName.text = activeExercise.exercise.name
            binding.tvTarget.text = "${activeExercise.exercise.targetSets}x${activeExercise.exercise.targetReps} | ${activeExercise.exercise.restSeconds}s rest"

            val setAdapter = ActiveSetAdapter(exerciseIndex, onSetCompleted, onSetUpdated)
            binding.rvSets.layoutManager = LinearLayoutManager(binding.root.context)
            binding.rvSets.adapter = setAdapter
            setAdapter.submitList(activeExercise.sets)

            binding.btnAddSet.setOnClickListener {
                onAddSet(exerciseIndex)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemActiveExerciseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(exercises[position], position)
    }

    override fun getItemCount() = exercises.size
}
