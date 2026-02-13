package com.example.gymtracker.ui.workout

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtracker.R
import com.example.gymtracker.databinding.ItemActiveSetBinding

class ActiveSetAdapter(
    private val exerciseIndex: Int,
    private val onSetCompleted: (exerciseIndex: Int, setIndex: Int) -> Unit,
    private val onSetUpdated: (exerciseIndex: Int, setIndex: Int, weight: Double, reps: Int) -> Unit
) : RecyclerView.Adapter<ActiveSetAdapter.ViewHolder>() {

    private val sets = mutableListOf<ActiveSet>()

    fun submitList(list: List<ActiveSet>) {
        sets.clear()
        sets.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemActiveSetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var weightWatcher: TextWatcher? = null
        private var repsWatcher: TextWatcher? = null

        fun bind(set: ActiveSet, setIndex: Int) {
            // Remove old watchers
            weightWatcher?.let { binding.etWeight.removeTextChangedListener(it) }
            repsWatcher?.let { binding.etReps.removeTextChangedListener(it) }

            binding.tvSetNumber.text = "S${set.setNumber}"

            if (set.weight > 0) {
                binding.etWeight.setText(if (set.weight % 1.0 == 0.0) set.weight.toInt().toString() else set.weight.toString())
            } else {
                binding.etWeight.text = null
            }

            if (set.reps > 0) {
                binding.etReps.setText(set.reps.toString())
            } else {
                binding.etReps.text = null
            }

            updateCompletionState(set.isCompleted)

            binding.btnComplete.setOnClickListener {
                onSetCompleted(exerciseIndex, setIndex)
            }

            weightWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val weight = s?.toString()?.toDoubleOrNull() ?: 0.0
                    val reps = binding.etReps.text?.toString()?.toIntOrNull() ?: 0
                    onSetUpdated(exerciseIndex, setIndex, weight, reps)
                }
            }

            repsWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val weight = binding.etWeight.text?.toString()?.toDoubleOrNull() ?: 0.0
                    val reps = s?.toString()?.toIntOrNull() ?: 0
                    onSetUpdated(exerciseIndex, setIndex, weight, reps)
                }
            }

            binding.etWeight.addTextChangedListener(weightWatcher)
            binding.etReps.addTextChangedListener(repsWatcher)
        }

        private fun updateCompletionState(isCompleted: Boolean) {
            val color = if (isCompleted) R.color.green_secondary else R.color.text_hint
            binding.btnComplete.setColorFilter(
                ContextCompat.getColor(binding.root.context, color)
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemActiveSetBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sets[position], position)
    }

    override fun getItemCount() = sets.size
}
