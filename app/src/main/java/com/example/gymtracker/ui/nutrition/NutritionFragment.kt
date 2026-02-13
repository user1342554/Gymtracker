package com.example.gymtracker.ui.nutrition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymtracker.GymTrackerApp
import com.example.gymtracker.R
import com.example.gymtracker.data.entity.FoodLogEntry
import com.example.gymtracker.databinding.FragmentNutritionBinding
import com.example.gymtracker.ui.common.ConfirmDeleteDialog
import com.example.gymtracker.ui.common.SwipeToDeleteCallback
import com.example.gymtracker.util.ViewModelFactory
import kotlinx.coroutines.launch
import java.util.Locale

class NutritionFragment : Fragment() {

    private var _binding: FragmentNutritionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NutritionViewModel by viewModels {
        ViewModelFactory((requireActivity().application as GymTrackerApp).database)
    }

    private lateinit var adapter: FoodLogAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNutritionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FoodLogAdapter { entry -> showDeleteConfirmation(entry) }
        binding.rvFoodLog.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFoodLog.adapter = adapter

        val swipeCallback = SwipeToDeleteCallback(requireContext()) { position ->
            val entry = adapter.currentList[position]
            showDeleteConfirmation(entry)
            adapter.notifyItemChanged(position)
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.rvFoodLog)

        binding.fabLogFood.setOnClickListener {
            LogFoodDialogFragment(
                onLog = { food, grams, mealType ->
                    viewModel.logFood(
                        food.id, food.name, grams,
                        food.caloriesPerGram, food.proteinPerGram,
                        food.carbsPerGram, food.fatPerGram, mealType
                    )
                }
            ).show(childFragmentManager, "log_food")
        }

        binding.btnFoodManager.setOnClickListener {
            findNavController().navigate(R.id.action_nutrition_to_food_manager)
        }

        binding.btnGoals.setOnClickListener {
            findNavController().navigate(R.id.action_nutrition_to_goals)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.foodLogEntries.collect { entries ->
                        adapter.submitList(entries)
                        binding.rvFoodLog.isVisible = entries.isNotEmpty()
                        binding.tvEmptyState.isVisible = entries.isEmpty()
                    }
                }
                launch {
                    viewModel.dailySummary.collect { summary ->
                        binding.tvCalories.text = String.format(Locale.US, "%.0f", summary?.totalCalories ?: 0.0)
                        binding.tvProtein.text = String.format(Locale.US, "%.1fg", summary?.totalProtein ?: 0.0)
                        binding.tvCarbs.text = String.format(Locale.US, "%.1fg", summary?.totalCarbs ?: 0.0)
                        binding.tvFat.text = String.format(Locale.US, "%.1fg", summary?.totalFat ?: 0.0)
                    }
                }
            }
        }
    }

    private fun showDeleteConfirmation(entry: FoodLogEntry) {
        ConfirmDeleteDialog(
            onConfirm = { viewModel.deleteFoodLogEntry(entry) }
        ).show(childFragmentManager, "confirm_delete")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
