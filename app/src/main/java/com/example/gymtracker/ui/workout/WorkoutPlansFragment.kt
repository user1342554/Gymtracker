package com.example.gymtracker.ui.workout

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymtracker.GymTrackerApp
import com.example.gymtracker.R
import com.example.gymtracker.data.relation.WorkoutPlanWithExercises
import com.example.gymtracker.databinding.FragmentWorkoutPlansBinding
import com.example.gymtracker.ui.common.ConfirmDeleteDialog
import com.example.gymtracker.util.ViewModelFactory
import kotlinx.coroutines.launch

class WorkoutPlansFragment : Fragment() {

    private var _binding: FragmentWorkoutPlansBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkoutPlansViewModel by viewModels {
        ViewModelFactory((requireActivity().application as GymTrackerApp).database)
    }

    private lateinit var adapter: WorkoutPlanAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWorkoutPlansBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = WorkoutPlanAdapter(
            onClick = { planId ->
                val bundle = Bundle().apply { putLong("planId", planId) }
                findNavController().navigate(R.id.action_workout_to_plan_detail, bundle)
            },
            onStartWorkout = { planId ->
                val bundle = Bundle().apply { putLong("planId", planId) }
                findNavController().navigate(R.id.navigation_active_workout, bundle)
            },
            onLongClick = { planWithExercises ->
                showDeleteConfirmation(planWithExercises)
            }
        )

        binding.rvPlans.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlans.adapter = adapter

        binding.fabAddPlan.setOnClickListener {
            AddEditPlanDialogFragment(
                onSave = { name, description ->
                    viewModel.addPlan(name, description, "#448AFF")
                }
            ).show(childFragmentManager, "add_plan")
        }

        binding.btnSchedule.setOnClickListener {
            findNavController().navigate(R.id.action_workout_to_schedule)
        }

        binding.btnHistory.setOnClickListener {
            findNavController().navigate(R.id.action_workout_to_history)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allPlansWithExercises.collect { plans ->
                    adapter.submitList(plans)
                    val hasData = plans.isNotEmpty()
                    binding.rvPlans.isVisible = hasData
                    binding.ivEmptyState.isVisible = !hasData
                    binding.tvEmptyState.isVisible = !hasData
                }
            }
        }
    }

    private fun showDeleteConfirmation(planWithExercises: WorkoutPlanWithExercises) {
        ConfirmDeleteDialog(
            onConfirm = { viewModel.deletePlan(planWithExercises.plan) }
        ).show(childFragmentManager, "confirm_delete")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
