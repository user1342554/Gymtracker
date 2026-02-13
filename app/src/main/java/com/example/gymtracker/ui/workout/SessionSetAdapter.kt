package com.example.gymtracker.ui.workout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtracker.R
import com.example.gymtracker.data.entity.WorkoutSet
import com.example.gymtracker.databinding.ItemSessionSetBinding
import java.util.Locale

class SessionSetAdapter : ListAdapter<WorkoutSet, SessionSetAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemSessionSetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(set: WorkoutSet) {
            binding.tvExerciseName.text = set.exerciseName
            binding.tvSetInfo.text = String.format(
                Locale.US, "Set %d: %.1f kg x %d", set.setNumber, set.weight, set.reps
            )
            val statusColor = if (set.isCompleted) R.color.green_secondary else R.color.text_hint
            binding.tvStatus.setTextColor(ContextCompat.getColor(binding.root.context, statusColor))
            binding.tvStatus.text = if (set.isCompleted) "Done" else "Skipped"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSessionSetBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<WorkoutSet>() {
        override fun areItemsTheSame(a: WorkoutSet, b: WorkoutSet) = a.id == b.id
        override fun areContentsTheSame(a: WorkoutSet, b: WorkoutSet) = a == b
    }
}
