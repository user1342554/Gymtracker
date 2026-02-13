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
import com.example.gymtracker.R
import com.example.gymtracker.data.relation.WeekdayWithPlan
import com.example.gymtracker.databinding.FragmentWeeklyScheduleBinding
import com.example.gymtracker.util.DateUtils
import com.example.gymtracker.util.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class WeeklyScheduleFragment : Fragment() {

    private var _binding: FragmentWeeklyScheduleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WeeklyScheduleViewModel by viewModels {
        ViewModelFactory((requireActivity().application as GymTrackerApp).database)
    }

    private lateinit var adapter: WeeklyScheduleAdapter

    private val dayNames = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWeeklyScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = WeeklyScheduleAdapter { dayOfWeek ->
            showPlanPicker(dayOfWeek)
        }

        binding.rvSchedule.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSchedule.adapter = adapter

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.assignmentsWithPlans.collect { assignments ->
                    val currentDay = DateUtils.getCurrentDayOfWeek()
                    val assignmentMap = assignments.associateBy { it.assignment.dayOfWeek }
                    val items = (1..7).map { day ->
                        WeekdayItem(
                            dayOfWeek = day,
                            dayName = dayNames[day - 1],
                            planName = assignmentMap[day]?.plan?.name,
                            isToday = day == currentDay
                        )
                    }
                    adapter.submitList(items)
                }
            }
        }
    }

    private fun showPlanPicker(dayOfWeek: Int) {
        val plans = viewModel.allPlans.value
        val names = arrayOf("Rest Day") + plans.map { it.name }.toTypedArray()

        MaterialAlertDialogBuilder(requireContext(), R.style.Theme_GymTracker_Dialog)
            .setTitle("${dayNames[dayOfWeek - 1]}")
            .setItems(names) { _, which ->
                if (which == 0) {
                    viewModel.clearDay(dayOfWeek)
                } else {
                    val plan = plans[which - 1]
                    viewModel.assignPlanToDay(dayOfWeek, plan.id)
                }
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
