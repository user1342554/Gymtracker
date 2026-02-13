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
import com.example.gymtracker.databinding.FragmentStatsWorkoutBinding
import com.example.gymtracker.util.ChartStyler
import com.example.gymtracker.util.DateUtils
import com.example.gymtracker.util.ViewModelFactory
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.launch
import java.util.Locale

class WorkoutStatsFragment : Fragment() {

    private var _binding: FragmentStatsWorkoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StatsViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    ) {
        ViewModelFactory((requireActivity().application as GymTrackerApp).database)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStatsWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ChartStyler.styleBarChart(binding.chartWorkouts, requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.workoutCount.collect { count ->
                        binding.tvWorkoutCount.text = count.toString()
                    }
                }
                launch {
                    viewModel.totalVolume.collect { volume ->
                        binding.tvTotalVolume.text = String.format(Locale.US, "%.0f kg", volume ?: 0.0)
                    }
                }
                launch {
                    viewModel.workoutSessions.collect { sessions ->
                        // Group by week and show bar chart
                        if (sessions.isNotEmpty()) {
                            val grouped = sessions.groupBy { it.date.substring(0, 7) } // group by month
                            val entries = grouped.entries.mapIndexed { index, entry ->
                                BarEntry(index.toFloat(), entry.value.size.toFloat())
                            }
                            val labels = grouped.keys.toList()
                            val dataSet = ChartStyler.createBarDataSet(entries, "Workouts", R.color.blue_primary, requireContext())
                            binding.chartWorkouts.data = BarData(dataSet)
                            ChartStyler.setXAxisLabels(binding.chartWorkouts, labels)
                            binding.chartWorkouts.invalidate()
                        }
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
