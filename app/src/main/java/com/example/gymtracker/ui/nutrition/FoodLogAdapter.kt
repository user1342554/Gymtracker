package com.example.gymtracker.ui.nutrition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtracker.data.entity.FoodLogEntry
import com.example.gymtracker.databinding.ItemFoodLogBinding
import java.util.Locale

class FoodLogAdapter(
    private val onLongClick: (FoodLogEntry) -> Unit
) : ListAdapter<FoodLogEntry, FoodLogAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemFoodLogBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: FoodLogEntry) {
            binding.tvFoodName.text = entry.foodNameSnapshot
            binding.tvGrams.text = String.format(Locale.US, "%.0fg | %s", entry.gramsEaten, entry.mealType)
            binding.tvCalories.text = String.format(Locale.US, "%.0f kcal", entry.caloriesSnapshot)
            binding.tvMacros.text = String.format(
                Locale.US, "P: %.1fg | C: %.1fg | F: %.1fg",
                entry.proteinSnapshot, entry.carbsSnapshot, entry.fatSnapshot
            )

            binding.root.setOnLongClickListener {
                onLongClick(entry)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFoodLogBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<FoodLogEntry>() {
        override fun areItemsTheSame(a: FoodLogEntry, b: FoodLogEntry) = a.id == b.id
        override fun areContentsTheSame(a: FoodLogEntry, b: FoodLogEntry) = a == b
    }
}
