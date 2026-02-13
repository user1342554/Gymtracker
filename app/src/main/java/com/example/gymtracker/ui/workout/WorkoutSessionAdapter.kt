package com.example.gymtracker.ui.workout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtracker.data.entity.WorkoutSession
import com.example.gymtracker.databinding.ItemWorkoutSessionBinding
import com.example.gymtracker.util.DateUtils

class WorkoutSessionAdapter(
    private val onClick: (Long) -> Unit,
    private val onLongClick: (WorkoutSession) -> Unit
) : ListAdapter<WorkoutSession, WorkoutSessionAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemWorkoutSessionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(session: WorkoutSession) {
            binding.tvPlanName.text = session.planNameSnapshot
            binding.tvDate.text = DateUtils.formatDate(session.date)

            val duration = if (session.endTime != null) {
                DateUtils.formatDuration(session.startTime, session.endTime)
            } else {
                "In progress"
            }
            binding.tvDuration.text = duration

            binding.root.setOnClickListener { onClick(session.id) }
            binding.root.setOnLongClickListener {
                onLongClick(session)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWorkoutSessionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<WorkoutSession>() {
        override fun areItemsTheSame(a: WorkoutSession, b: WorkoutSession) = a.id == b.id
        override fun areContentsTheSame(a: WorkoutSession, b: WorkoutSession) = a == b
    }
}
