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
import com.example.gymtracker.databinding.FragmentStatsNutritionBinding
import com.example.gymtracker.util.ChartStyler
import com.example.gymtracker.util.DateUtils
import com.example.gymtracker.util.ViewModelFactory
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import kotlinx.coroutines.launch

class NutritionStatsFragment : Fragment() {

    private var _binding: FragmentStatsNutritionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StatsViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    ) {
        ViewModelFactory((requireActivity().application as GymTrackerApp).database)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStatsNutritionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ChartStyler.styleLineChart(binding.chartCalories, requireContext())
        ChartStyler.styleLineChart(binding.chartProtein, requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.nutritionSummaries.collect { summaries ->
                    if (summaries.isNotEmpty()) {
                        // Calories chart
                        val calEntries = summaries.mapIndexed { i, s ->
                            Entry(i.toFloat(), s.totalCalories.toFloat())
                        }
                        val labels = summaries.map { DateUtils.formatDateShort(it.date) }

                        val calDataSet = ChartStyler.createLineDataSet(calEntries, "Calories", R.color.blue_primary, requireContext())
                        binding.chartCalories.data = LineData(calDataSet)
                        ChartStyler.setXAxisLabels(binding.chartCalories, labels)
                        binding.chartCalories.invalidate()

                        // Protein chart
                        val proEntries = summaries.mapIndexed { i, s ->
                            Entry(i.toFloat(), s.totalProtein.toFloat())
                        }
                        val proDataSet = ChartStyler.createLineDataSet(proEntries, "Protein", R.color.green_secondary, requireContext())
                        binding.chartProtein.data = LineData(proDataSet)
                        ChartStyler.setXAxisLabels(binding.chartProtein, labels)
                        binding.chartProtein.invalidate()
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
