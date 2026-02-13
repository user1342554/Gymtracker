package com.example.gymtracker.ui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.gymtracker.R
import com.example.gymtracker.data.entity.WorkoutPlan
import com.example.gymtracker.databinding.DialogAddEditPlanBinding

class AddEditPlanDialogFragment(
    private val existingPlan: WorkoutPlan? = null,
    private val onSave: (name: String, description: String) -> Unit
) : DialogFragment() {

    private var _binding: DialogAddEditPlanBinding? = null
    private val binding get() = _binding!!

    override fun getTheme(): Int = R.style.Theme_GymTracker_Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogAddEditPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        existingPlan?.let {
            binding.tvDialogTitle.setText(R.string.edit_workout_plan)
            binding.etPlanName.setText(it.name)
            binding.etDescription.setText(it.description)
        }

        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnSave.setOnClickListener {
            val name = binding.etPlanName.text?.toString()?.trim()
            if (name.isNullOrBlank()) {
                binding.etPlanName.error = "Enter plan name"
                return@setOnClickListener
            }
            val description = binding.etDescription.text?.toString()?.trim() ?: ""
            onSave(name, description)
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
