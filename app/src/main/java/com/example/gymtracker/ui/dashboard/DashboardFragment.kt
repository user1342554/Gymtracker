package com.example.gymtracker.ui.dashboard

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
import com.example.gymtracker.GymTrackerApp
import com.example.gymtracker.R
import com.example.gymtracker.databinding.FragmentDashboardBinding
import com.example.gymtracker.util.ViewModelFactory
import kotlinx.coroutines.launch
import java.util.Locale

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels {
        ViewModelFactory((requireActivity().application as GymTrackerApp).database)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    // Workout card
                    if (state.todayPlan != null) {
                        binding.tvTodayPlan.text = state.todayPlan.name
                        if (state.todayWorkoutCompleted) {
                            binding.tvWorkoutStatus.text = getString(R.string.workout_completed)
                            binding.tvWorkoutStatus.setTextColor(requireContext().getColor(R.color.green_secondary))
                            binding.btnStartWorkout.isVisible = false
                        } else {
                            binding.tvWorkoutStatus.text = "Tap to start"
                            binding.tvWorkoutStatus.setTextColor(requireContext().getColor(R.color.text_hint))
                            binding.btnStartWorkout.isVisible = true
                            binding.btnStartWorkout.setOnClickListener {
                                val bundle = Bundle().apply { putLong("planId", state.todayPlan.id) }
                                findNavController().navigate(R.id.action_dashboard_to_active_workout, bundle)
                            }
                        }
                    } else {
                        binding.tvTodayPlan.text = getString(R.string.dashboard_rest_day)
                        binding.tvWorkoutStatus.text = getString(R.string.dashboard_no_workout)
                        binding.tvWorkoutStatus.setTextColor(requireContext().getColor(R.color.text_hint))
                        binding.btnStartWorkout.isVisible = false
                    }

                    // Nutrition card
                    val nutrition = state.todayNutrition
                    binding.tvDashCalories.text = String.format(Locale.US, "%.0f", nutrition?.totalCalories ?: 0.0)
                    binding.tvDashProtein.text = String.format(Locale.US, "%.1fg", nutrition?.totalProtein ?: 0.0)
                    binding.tvDashCarbs.text = String.format(Locale.US, "%.1fg", nutrition?.totalCarbs ?: 0.0)
                    binding.tvDashFat.text = String.format(Locale.US, "%.1fg", nutrition?.totalFat ?: 0.0)

                    // Weight card
                    binding.tvDashWeight.text = state.latestWeight?.let {
                        String.format(Locale.US, "%.1f kg", it.weightKg)
                    } ?: "--"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
