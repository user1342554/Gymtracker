package com.example.gymtracker.ui.nutrition

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymtracker.GymTrackerApp
import com.example.gymtracker.R
import com.example.gymtracker.data.entity.Food
import com.example.gymtracker.databinding.DialogLogFoodBinding
import com.example.gymtracker.util.ViewModelFactory
import kotlinx.coroutines.launch
import java.util.Locale

class LogFoodDialogFragment(
    private val onLog: (food: Food, grams: Double, mealType: String) -> Unit
) : DialogFragment() {

    private var _binding: DialogLogFoodBinding? = null
    private val binding get() = _binding!!
    private var selectedFood: Food? = null
    private var allFoods: List<Food> = emptyList()

    private val viewModel: NutritionViewModel by viewModels(
        ownerProducer = { requireParentFragment() },
        factoryProducer = { ViewModelFactory((requireActivity().application as GymTrackerApp).database) }
    )

    override fun getTheme(): Int = R.style.Theme_GymTracker_Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogLogFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val foodAdapter = FoodAdapter(
            onClick = { food ->
                selectedFood = food
                binding.etSearch.setText(food.name)
                updatePreview()
            }
        )
        binding.rvFoodList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFoodList.adapter = foodAdapter

        // Observe live food list from ViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allFoods.collect { foods ->
                    allFoods = foods
                    val query = binding.etSearch.text?.toString()?.lowercase() ?: ""
                    val filtered = if (query.isBlank()) foods
                        else foods.filter { it.name.lowercase().contains(query) }
                    foodAdapter.submitList(filtered)
                }
            }
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString()?.lowercase() ?: ""
                val filtered = if (query.isBlank()) allFoods
                    else allFoods.filter { it.name.lowercase().contains(query) }
                foodAdapter.submitList(filtered)
            }
        })

        binding.etGrams.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { updatePreview() }
        })

        binding.btnAddNewFood.setOnClickListener {
            AddEditFoodDialogFragment(
                onSave = { name, cal, pro, carb, fat ->
                    viewModel.addFood(name, cal, pro, carb, fat)
                }
            ).show(childFragmentManager, "add_food_inline")
        }

        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnLog.setOnClickListener {
            val food = selectedFood
            if (food == null) {
                binding.etSearch.error = "Select a food"
                return@setOnClickListener
            }
            val grams = binding.etGrams.text?.toString()?.toDoubleOrNull()
            if (grams == null || grams <= 0) {
                binding.etGrams.error = "Enter grams"
                return@setOnClickListener
            }
            val mealType = when (binding.chipGroupMealType.checkedChipId) {
                R.id.chipBreakfast -> "Breakfast"
                R.id.chipLunch -> "Lunch"
                R.id.chipDinner -> "Dinner"
                else -> "Snack"
            }
            onLog(food, grams, mealType)
            dismiss()
        }
    }

    private fun updatePreview() {
        val food = selectedFood ?: return
        val grams = binding.etGrams.text?.toString()?.toDoubleOrNull() ?: return

        val cal = food.caloriesPerGram * grams
        val pro = food.proteinPerGram * grams
        val carb = food.carbsPerGram * grams
        val fat = food.fatPerGram * grams

        binding.tvPreview.text = String.format(
            Locale.US, "%.0f kcal | P: %.1fg | C: %.1fg | F: %.1fg", cal, pro, carb, fat
        )
        binding.tvPreview.isVisible = true
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
