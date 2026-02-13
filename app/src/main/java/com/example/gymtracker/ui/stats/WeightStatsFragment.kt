package com.example.gymtracker.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.gymtracker.GymTrackerApp
import com.example.gymtracker.R
import com.example.gymtracker.databinding.FragmentStatsWeightBinding
import com.example.gymtracker.util.ChartStyler
import com.example.gymtracker.util.DateUtils
import com.example.gymtracker.util.ViewModelFactory
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import kotlinx.coroutines.launch

class WeightStatsFragment : Fragment() {

    private var _binding: FragmentStatsWeightBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StatsViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    ) {
        ViewModelFactory((requireActivity().application as GymTrackerApp).database)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStatsWeightBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ChartStyler.styleLineChart(binding.chartWeight, requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.weightEntries.collect { entries ->
                    if (entries.isNotEmpty()) {
                        val chartEntries = entries.mapIndexed { index, entry ->
                            Entry(index.toFloat(), entry.weightKg.toFloat())
                        }
                        val labels = entries.map { DateUtils.formatDateShort(it.date) }

                        val dataSet = ChartStyler.createLineDataSet(
                            chartEntries, "Weight", R.color.blue_primary, requireContext()
                        )
                        binding.chartWeight.data = LineData(dataSet)
                        ChartStyler.setXAxisLabels(binding.chartWeight, labels)
                        binding.chartWeight.invalidate()
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
