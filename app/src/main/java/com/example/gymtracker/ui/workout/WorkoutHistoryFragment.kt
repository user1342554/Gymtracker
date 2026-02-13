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
import com.example.gymtracker.data.entity.WorkoutSession
import com.example.gymtracker.databinding.FragmentWorkoutHistoryBinding
import com.example.gymtracker.ui.common.ConfirmDeleteDialog
import com.example.gymtracker.ui.common.SwipeToDeleteCallback
import com.example.gymtracker.util.ViewModelFactory
import kotlinx.coroutines.launch

class WorkoutHistoryFragment : Fragment() {

    private var _binding: FragmentWorkoutHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkoutHistoryViewModel by viewModels {
        ViewModelFactory((requireActivity().application as GymTrackerApp).database)
    }

    private lateinit var adapter: WorkoutSessionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWorkoutHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = WorkoutSessionAdapter(
            onClick = { sessionId ->
                val bundle = Bundle().apply { putLong("sessionId", sessionId) }
                findNavController().navigate(R.id.action_history_to_session_detail, bundle)
            },
            onLongClick = { session -> showDeleteConfirmation(session) }
        )

        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = adapter

        val swipeCallback = SwipeToDeleteCallback(requireContext()) { position ->
            val session = adapter.currentList[position]
            showDeleteConfirmation(session)
            adapter.notifyItemChanged(position)
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.rvHistory)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.completedSessions.collect { sessions ->
                    adapter.submitList(sessions)
                    val hasData = sessions.isNotEmpty()
                    binding.rvHistory.isVisible = hasData
                    binding.ivEmptyState.isVisible = !hasData
                    binding.tvEmptyState.isVisible = !hasData
                }
            }
        }
    }

    private fun showDeleteConfirmation(session: WorkoutSession) {
        ConfirmDeleteDialog(
            onConfirm = { viewModel.deleteSession(session) }
        ).show(childFragmentManager, "confirm_delete")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
