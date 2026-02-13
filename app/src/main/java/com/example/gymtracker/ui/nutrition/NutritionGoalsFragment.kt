package com.example.gymtracker.ui.nutrition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.gymtracker.GymTrackerApp
import com.example.gymtracker.databinding.FragmentNutritionGoalsBinding
import com.example.gymtracker.util.ViewModelFactory
import kotlinx.coroutines.launch

class NutritionGoalsFragment : Fragment() {

    private var _binding: FragmentNutritionGoalsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NutritionGoalsViewModel by viewModels {
        ViewModelFactory((requireActivity().application as GymTrackerApp).database)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNutritionGoalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnSave.setOnClickListener {
            val cal = binding.etCalories.text?.toString()?.toIntOrNull() ?: 2000
            val pro = binding.etProtein.text?.toString()?.toIntOrNull() ?: 150
            val carb = binding.etCarbs.text?.toString()?.toIntOrNull() ?: 250
            val fat = binding.etFat.text?.toString()?.toIntOrNull() ?: 65
            viewModel.saveGoal(cal, pro, carb, fat)
            Toast.makeText(requireContext(), "Goals saved", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.goal.collect { goal ->
                    goal?.let {
                        binding.etCalories.setText(it.dailyCalories.toString())
                        binding.etProtein.setText(it.dailyProtein.toString())
                        binding.etCarbs.setText(it.dailyCarbs.toString())
                        binding.etFat.setText(it.dailyFat.toString())
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
