package com.example.gymtracker.ui.weight

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymtracker.GymTrackerApp
import com.example.gymtracker.R
import com.example.gymtracker.data.entity.BodyWeightEntry
import com.example.gymtracker.databinding.FragmentBodyWeightBinding
import com.example.gymtracker.ui.common.ConfirmDeleteDialog
import com.example.gymtracker.ui.common.SwipeToDeleteCallback
import com.example.gymtracker.util.ChartStyler
import com.example.gymtracker.util.DateUtils
import com.example.gymtracker.util.ViewModelFactory
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import kotlinx.coroutines.launch
import java.util.Locale

class BodyWeightFragment : Fragment() {

    private var _binding: FragmentBodyWeightBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BodyWeightViewModel by viewModels {
        ViewModelFactory((requireActivity().application as GymTrackerApp).database)
    }

    private lateinit var adapter: BodyWeightAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBodyWeightBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BodyWeightAdapter { entry ->
            showDeleteConfirmation(entry)
        }

        binding.rvWeightEntries.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWeightEntries.adapter = adapter

        val swipeCallback = SwipeToDeleteCallback(requireContext()) { position ->
            val entry = adapter.currentList[position]
            showDeleteConfirmation(entry)
            adapter.notifyItemChanged(position)
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.rvWeightEntries)

        ChartStyler.styleLineChart(binding.weightChart, requireContext())

        binding.fabLogWeight.setOnClickListener {
            LogWeightDialogFragment { weight, notes ->
                viewModel.logWeight(weight, notes)
            }.show(childFragmentManager, "log_weight")
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.allEntries.collect { entries ->
                        adapter.submitList(entries)
                        val hasData = entries.isNotEmpty()
                        binding.rvWeightEntries.isVisible = hasData
                        binding.chartCard.isVisible = hasData
                        binding.ivEmptyState.isVisible = !hasData
                        binding.tvEmptyState.isVisible = !hasData

                        if (hasData) {
                            updateChart(entries)
                        }
                    }
                }
                launch {
                    viewModel.latestEntry.collect { entry ->
                        binding.tvCurrentWeight.text = entry?.let {
                            String.format(Locale.US, "%.1f kg", it.weightKg)
                        } ?: "--"
                    }
                }
            }
        }
    }

    private fun updateChart(entries: List<BodyWeightEntry>) {
        val sorted = entries.reversed()
        val chartEntries = sorted.mapIndexed { index, entry ->
            Entry(index.toFloat(), entry.weightKg.toFloat())
        }
        val labels = sorted.map { DateUtils.formatDateShort(it.date) }

        val dataSet = ChartStyler.createLineDataSet(
            chartEntries, "Weight", R.color.blue_primary, requireContext()
        )
        binding.weightChart.data = LineData(dataSet)
        ChartStyler.setXAxisLabels(binding.weightChart, labels)
        binding.weightChart.invalidate()
    }

    private fun showDeleteConfirmation(entry: BodyWeightEntry) {
        ConfirmDeleteDialog(
            onConfirm = { viewModel.deleteEntry(entry) }
        ).show(childFragmentManager, "confirm_delete")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
