package com.example.gymtracker.ui.weight

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtracker.R
import com.example.gymtracker.data.entity.BodyWeightEntry
import com.example.gymtracker.databinding.ItemBodyWeightBinding
import com.example.gymtracker.util.DateUtils
import java.util.Locale

class BodyWeightAdapter(
    private val onDeleteClick: (BodyWeightEntry) -> Unit
) : ListAdapter<BodyWeightEntry, BodyWeightAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemBodyWeightBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: BodyWeightEntry, previousEntry: BodyWeightEntry?) {
            binding.tvWeight.text = String.format(Locale.US, "%.1f kg", entry.weightKg)
            binding.tvDate.text = DateUtils.formatDate(entry.date)

            if (entry.notes.isNotBlank()) {
                binding.tvNotes.text = entry.notes
                binding.tvNotes.isVisible = true
            } else {
                binding.tvNotes.isVisible = false
            }

            // Show delta from previous entry
            if (previousEntry != null) {
                val delta = entry.weightKg - previousEntry.weightKg
                val sign = if (delta >= 0) "+" else ""
                binding.tvDelta.text = String.format(Locale.US, "%s%.1f", sign, delta)
                binding.tvDelta.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        if (delta > 0) R.color.orange_tertiary
                        else if (delta < 0) R.color.green_secondary
                        else R.color.text_hint
                    )
                )
                binding.tvDelta.isVisible = true
            } else {
                binding.tvDelta.isVisible = false
            }

            binding.root.setOnLongClickListener {
                onDeleteClick(entry)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBodyWeightBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = getItem(position)
        // Entries are sorted DESC, so "previous" is the next index (earlier date)
        val previousEntry = if (position < itemCount - 1) getItem(position + 1) else null
        holder.bind(entry, previousEntry)
    }

    class DiffCallback : DiffUtil.ItemCallback<BodyWeightEntry>() {
        override fun areItemsTheSame(oldItem: BodyWeightEntry, newItem: BodyWeightEntry) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: BodyWeightEntry, newItem: BodyWeightEntry) =
            oldItem == newItem
    }
}
