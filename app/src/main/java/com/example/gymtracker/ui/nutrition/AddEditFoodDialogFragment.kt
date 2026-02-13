package com.example.gymtracker.ui.nutrition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.gymtracker.R
import com.example.gymtracker.data.entity.Food
import com.example.gymtracker.databinding.DialogAddEditFoodBinding

class AddEditFoodDialogFragment(
    private val existingFood: Food? = null,
    private val onSave: (name: String, caloriesPer100g: Double, proteinPer100g: Double, carbsPer100g: Double, fatPer100g: Double) -> Unit
) : DialogFragment() {

    private var _binding: DialogAddEditFoodBinding? = null
    private val binding get() = _binding!!

    override fun getTheme(): Int = R.style.Theme_GymTracker_Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogAddEditFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        existingFood?.let { food ->
            binding.tvDialogTitle.setText(R.string.edit_food)
            binding.etFoodName.setText(food.name)
            binding.etCalories.setText("%.0f".format(food.caloriesPerGram * 100))
            binding.etProtein.setText("%.1f".format(food.proteinPerGram * 100))
            binding.etCarbs.setText("%.1f".format(food.carbsPerGram * 100))
            binding.etFat.setText("%.1f".format(food.fatPerGram * 100))
        }

        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnSave.setOnClickListener {
            val name = binding.etFoodName.text?.toString()?.trim()
            if (name.isNullOrBlank()) {
                binding.etFoodName.error = "Enter food name"
                return@setOnClickListener
            }
            val cal = binding.etCalories.text?.toString()?.toDoubleOrNull() ?: 0.0
            val pro = binding.etProtein.text?.toString()?.toDoubleOrNull() ?: 0.0
            val carb = binding.etCarbs.text?.toString()?.toDoubleOrNull() ?: 0.0
            val fat = binding.etFat.text?.toString()?.toDoubleOrNull() ?: 0.0
            onSave(name, cal, pro, carb, fat)
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
