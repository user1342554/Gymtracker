package com.example.gymtracker.ui.workout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtracker.data.relation.WorkoutPlanWithExercises
import com.example.gymtracker.databinding.ItemWorkoutPlanBinding

class WorkoutPlanAdapter(
    private val onClick: (Long) -> Unit,
    private val onStartWorkout: (Long) -> Unit,
    private val onLongClick: (WorkoutPlanWithExercises) -> Unit
) : ListAdapter<WorkoutPlanWithExercises, WorkoutPlanAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemWorkoutPlanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WorkoutPlanWithExercises) {
            binding.tvPlanName.text = item.plan.name
            binding.tvExerciseCount.text = "${item.exercises.size} exercises"

            if (item.plan.description.isNotBlank()) {
                binding.tvDescription.text = item.plan.description
                binding.tvDescription.isVisible = true
            } else {
                binding.tvDescription.isVisible = false
            }

            try {
                binding.colorIndicator.setBackgroundColor(Color.parseColor(item.plan.colorHex))
            } catch (e: Exception) {
                binding.colorIndicator.setBackgroundColor(Color.parseColor("#448AFF"))
            }

            binding.root.setOnClickListener { onClick(item.plan.id) }
            binding.btnStartWorkout.setOnClickListener { onStartWorkout(item.plan.id) }
            binding.root.setOnLongClickListener {
                onLongClick(item)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWorkoutPlanBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<WorkoutPlanWithExercises>() {
        override fun areItemsTheSame(a: WorkoutPlanWithExercises, b: WorkoutPlanWithExercises) =
            a.plan.id == b.plan.id

        override fun areContentsTheSame(a: WorkoutPlanWithExercises, b: WorkoutPlanWithExercises) =
            a == b
    }
}
