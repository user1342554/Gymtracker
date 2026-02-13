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
import com.example.gymtracker.databinding.FragmentActiveWorkoutBinding
import com.example.gymtracker.ui.common.ConfirmDeleteDialog
import com.example.gymtracker.util.ViewModelFactory
import kotlinx.coroutines.launch

class ActiveWorkoutFragment : Fragment() {

    private var _binding: FragmentActiveWorkoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ActiveWorkoutViewModel by viewModels {
        ViewModelFactory((requireActivity().application as GymTrackerApp).database)
    }

    private lateinit var exerciseAdapter: ActiveExerciseAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentActiveWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val planId = arguments?.getLong("planId") ?: -1L
        viewModel.startWorkout(planId)

        exerciseAdapter = ActiveExerciseAdapter(
            onSetCompleted = { exerciseIndex, setIndex ->
                viewModel.completeSet(exerciseIndex, setIndex)
            },
            onSetUpdated = { exerciseIndex, setIndex, weight, reps ->
                viewModel.updateSet(exerciseIndex, setIndex, weight, reps)
            },
            onAddSet = { exerciseIndex ->
                viewModel.addSet(exerciseIndex)
            }
        )

        binding.rvExercises.layoutManager = LinearLayoutManager(requireContext())
        binding.rvExercises.adapter = exerciseAdapter

        binding.btnFinish.setOnClickListener {
            viewModel.finishWorkout {
                findNavController().popBackStack()
            }
        }

        binding.btnDiscard.setOnClickListener {
            ConfirmDeleteDialog(
                onConfirm = {
                    viewModel.discardWorkout {
                        findNavController().popBackStack()
                    }
                },
                title = R.string.discard_workout,
                message = R.string.discard_workout_message
            ).show(childFragmentManager, "confirm_discard")
        }

        binding.btnSkipRest.setOnClickListener {
            viewModel.stopRestTimer()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.planName.collect { name ->
                        binding.tvWorkoutName.text = name
                    }
                }
                launch {
                    viewModel.exercises.collect { exercises ->
                        exerciseAdapter.submitList(exercises)
                    }
                }
                launch {
                    viewModel.isRestTimerRunning.collect { running ->
                        binding.restTimerCard.isVisible = running
                    }
                }
                launch {
                    viewModel.restTimerSeconds.collect { seconds ->
                        val mins = seconds / 60
                        val secs = seconds % 60
                        binding.tvRestTimer.text = String.format("%d:%02d", mins, secs)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
