package com.example.gymtracker.ui.workout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtracker.data.entity.Exercise
import com.example.gymtracker.databinding.ItemExerciseBinding

class ExerciseAdapter(
    private val onEdit: (Exercise) -> Unit,
    private val onDelete: (Exercise) -> Unit
) : ListAdapter<Exercise, ExerciseAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(exercise: Exercise) {
            binding.tvExerciseName.text = exercise.name
            binding.tvSetsReps.text = "${exercise.targetSets} sets x ${exercise.targetReps} reps"
            binding.tvRest.text = "${exercise.restSeconds}s rest"

            if (exercise.notes.isNotBlank()) {
                binding.tvNotes.text = exercise.notes
                binding.tvNotes.isVisible = true
            } else {
                binding.tvNotes.isVisible = false
            }

            binding.btnEdit.setOnClickListener { onEdit(exercise) }
            binding.btnDelete.setOnClickListener { onDelete(exercise) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemExerciseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(a: Exercise, b: Exercise) = a.id == b.id
        override fun areContentsTheSame(a: Exercise, b: Exercise) = a == b
    }
}
