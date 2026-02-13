package com.example.gymtracker.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gymtracker.GymTrackerApp
import com.example.gymtracker.R
import com.example.gymtracker.databinding.FragmentStatsBinding
import com.example.gymtracker.util.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StatsViewModel by viewModels {
        ViewModelFactory((requireActivity().application as GymTrackerApp).database)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = StatsPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.workout_stats)
                1 -> getString(R.string.nutrition_stats)
                2 -> getString(R.string.weight_stats)
                else -> ""
            }
        }.attach()

        binding.chipGroupPeriod.setOnCheckedStateChangeListener { _, checkedIds ->
            val days = when {
                checkedIds.contains(R.id.chip7Days) -> 7
                checkedIds.contains(R.id.chip90Days) -> 90
                else -> 30
            }
            viewModel.setPeriod(days)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class StatsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount() = 3
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> WorkoutStatsFragment()
                1 -> NutritionStatsFragment()
                2 -> WeightStatsFragment()
                else -> WorkoutStatsFragment()
            }
        }
    }
}
