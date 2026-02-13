package com.example.gymtracker.ui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymtracker.GymTrackerApp
import com.example.gymtracker.databinding.FragmentSessionDetailBinding
import com.example.gymtracker.util.DateUtils
import com.example.gymtracker.util.ViewModelFactory
import kotlinx.coroutines.launch
import java.util.Locale

class SessionDetailFragment : Fragment() {

    private var _binding: FragmentSessionDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SessionDetailViewModel by viewModels {
        ViewModelFactory((requireActivity().application as GymTrackerApp).database)
    }

    private lateinit var adapter: SessionSetAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSessionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sessionId = arguments?.getLong("sessionId") ?: return
        viewModel.loadSession(sessionId)

        adapter = SessionSetAdapter()
        binding.rvSets.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSets.adapter = adapter

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sessionWithSets.collect { sessionWithSets ->
                    sessionWithSets?.let { sws ->
                        binding.tvPlanName.text = sws.session.planNameSnapshot
                        val date = DateUtils.formatDate(sws.session.date)
                        val duration = if (sws.session.endTime != null) {
                            DateUtils.formatDuration(sws.session.startTime, sws.session.endTime)
                        } else ""
                        val totalVolume = sws.sets.filter { it.isCompleted }
                            .sumOf { it.weight * it.reps }
                        val completedSets = sws.sets.count { it.isCompleted }

                        binding.tvSessionInfo.text = String.format(
                            Locale.US,
                            "%s | %s | %d sets | %.0f kg volume",
                            date, duration, completedSets, totalVolume
                        )

                        adapter.submitList(sws.sets)
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
