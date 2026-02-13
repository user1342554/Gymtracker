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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymtracker.GymTrackerApp
import com.example.gymtracker.R
import com.example.gymtracker.data.entity.Exercise
import com.example.gymtracker.databinding.FragmentWorkoutPlanDetailBinding
import com.example.gymtracker.ui.common.ConfirmDeleteDialog
import com.example.gymtracker.ui.common.SwipeToDeleteCallback
import com.example.gymtracker.util.ViewModelFactory
import kotlinx.coroutines.launch

class WorkoutPlanDetailFragment : Fragment() {

    private var _binding: FragmentWorkoutPlanDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkoutPlanDetailViewModel by viewModels {
        ViewModelFactory((requireActivity().application as GymTrackerApp).database)
    }

    private lateinit var adapter: ExerciseAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWorkoutPlanDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val planId = arguments?.getLong("planId") ?: return
        viewModel.loadPlan(planId)

        adapter = ExerciseAdapter(
            onEdit = { exercise -> showEditExerciseDialog(exercise) },
            onDelete = { exercise -> showDeleteConfirmation(exercise) }
        )

        binding.rvExercises.layoutManager = LinearLayoutManager(requireContext())
        binding.rvExercises.adapter = adapter

        val swipeCallback = SwipeToDeleteCallback(requireContext()) { position ->
            val exercise = adapter.currentList[position]
            showDeleteConfirmation(exercise)
            adapter.notifyItemChanged(position)
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.rvExercises)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnStartWorkout.setOnClickListener {
            val bundle = Bundle().apply { putLong("planId", planId) }
            findNavController().navigate(R.id.action_plan_detail_to_active_workout, bundle)
        }

        binding.fabAddExercise.setOnClickListener {
            AddEditExerciseDialogFragment(
                onSave = { name, sets, reps, rest, notes ->
                    viewModel.addExercise(name, sets, reps, rest, notes)
                }
            ).show(childFragmentManager, "add_exercise")
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.plan.collect { plan ->
                        plan?.let {
                            binding.tvPlanName.text = it.name
                            if (it.description.isNotBlank()) {
                                binding.tvDescription.text = it.description
                                binding.tvDescription.isVisible = true
                            }
                        }
                    }
                }
                launch {
                    viewModel.exercises.collect { exercises ->
                        adapter.submitList(exercises)
                        val hasData = exercises.isNotEmpty()
                        binding.rvExercises.isVisible = hasData
                        binding.ivEmptyState.isVisible = !hasData
                        binding.tvEmptyState.isVisible = !hasData
                    }
                }
            }
        }
    }

    private fun showEditExerciseDialog(exercise: Exercise) {
        AddEditExerciseDialogFragment(
            existingExercise = exercise,
            onSave = { name, sets, reps, rest, notes ->
                viewModel.updateExercise(
                    exercise.copy(
                        name = name,
                        targetSets = sets,
                        targetReps = reps,
                        restSeconds = rest,
                        notes = notes
                    )
                )
            }
        ).show(childFragmentManager, "edit_exercise")
    }

    private fun showDeleteConfirmation(exercise: Exercise) {
        ConfirmDeleteDialog(
            onConfirm = { viewModel.deleteExercise(exercise) }
        ).show(childFragmentManager, "confirm_delete")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
