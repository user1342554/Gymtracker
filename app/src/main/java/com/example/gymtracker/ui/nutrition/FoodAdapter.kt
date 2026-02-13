package com.example.gymtracker.ui.nutrition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtracker.data.entity.Food
import com.example.gymtracker.databinding.ItemFoodBinding
import java.util.Locale

class FoodAdapter(
    private val onClick: ((Food) -> Unit)? = null,
    private val onLongClick: ((Food) -> Unit)? = null
) : ListAdapter<Food, FoodAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(food: Food) {
            binding.tvFoodName.text = food.name
            binding.tvCalories.text = String.format(Locale.US, "%.0f kcal", food.caloriesPerGram * 100)
            binding.tvMacros.text = String.format(
                Locale.US, "P: %.1fg | C: %.1fg | F: %.1fg",
                food.proteinPerGram * 100, food.carbsPerGram * 100, food.fatPerGram * 100
            )

            binding.root.setOnClickListener { onClick?.invoke(food) }
            binding.root.setOnLongClickListener {
                onLongClick?.invoke(food)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFoodBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(a: Food, b: Food) = a.id == b.id
        override fun areContentsTheSame(a: Food, b: Food) = a == b
    }
}
