package com.example.gymtracker.ui.nutrition

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.gymtracker.data.entity.Food
import com.example.gymtracker.databinding.FragmentFoodManagerBinding
import com.example.gymtracker.ui.common.ConfirmDeleteDialog
import com.example.gymtracker.ui.common.SwipeToDeleteCallback
import com.example.gymtracker.util.ViewModelFactory
import kotlinx.coroutines.launch

class FoodManagerFragment : Fragment() {

    private var _binding: FragmentFoodManagerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FoodManagerViewModel by viewModels {
        ViewModelFactory((requireActivity().application as GymTrackerApp).database)
    }

    private lateinit var adapter: FoodAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFoodManagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FoodAdapter(
            onClick = { food -> showEditFoodDialog(food) },
            onLongClick = { food -> showDeleteConfirmation(food) }
        )
        binding.rvFoods.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFoods.adapter = adapter

        val swipeCallback = SwipeToDeleteCallback(requireContext()) { position ->
            val food = adapter.currentList[position]
            showDeleteConfirmation(food)
            adapter.notifyItemChanged(position)
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.rvFoods)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.setSearchQuery(s?.toString() ?: "")
            }
        })

        binding.fabAddFood.setOnClickListener {
            AddEditFoodDialogFragment(
                onSave = { name, cal, pro, carb, fat ->
                    viewModel.addFood(name, cal, pro, carb, fat)
                }
            ).show(childFragmentManager, "add_food")
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.foods.collect { foods ->
                    adapter.submitList(foods)
                    binding.rvFoods.isVisible = foods.isNotEmpty()
                    binding.tvEmptyState.isVisible = foods.isEmpty()
                }
            }
        }
    }

    private fun showEditFoodDialog(food: Food) {
        AddEditFoodDialogFragment(
            existingFood = food,
            onSave = { name, cal, pro, carb, fat ->
                viewModel.updateFood(food, name, cal, pro, carb, fat)
            }
        ).show(childFragmentManager, "edit_food")
    }

    private fun showDeleteConfirmation(food: Food) {
        ConfirmDeleteDialog(
            onConfirm = { viewModel.deleteFood(food) }
        ).show(childFragmentManager, "confirm_delete")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
