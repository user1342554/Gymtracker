package com.example.gymtracker.ui.workout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtracker.databinding.ItemWeekdayScheduleBinding

data class WeekdayItem(
    val dayOfWeek: Int,
    val dayName: String,
    val planName: String?,
    val isToday: Boolean
)

class WeeklyScheduleAdapter(
    private val onAssignClick: (dayOfWeek: Int) -> Unit
) : RecyclerView.Adapter<WeeklyScheduleAdapter.ViewHolder>() {

    private val items = mutableListOf<WeekdayItem>()

    fun submitList(list: List<WeekdayItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemWeekdayScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WeekdayItem) {
            binding.tvDayName.text = item.dayName
            binding.tvPlanName.text = item.planName ?: "Rest Day"

            if (item.isToday) {
                binding.tvDayName.setTextColor(
                    binding.root.context.getColor(com.example.gymtracker.R.color.blue_primary)
                )
            } else {
                binding.tvDayName.setTextColor(
                    binding.root.context.getColor(com.example.gymtracker.R.color.text_primary)
                )
            }

            binding.btnAssign.setOnClickListener {
                onAssignClick(item.dayOfWeek)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWeekdayScheduleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
