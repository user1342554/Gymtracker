package com.example.gymtracker.ui.weight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.gymtracker.R
import com.example.gymtracker.databinding.DialogLogWeightBinding

class LogWeightDialogFragment(
    private val onSave: (weight: Double, notes: String) -> Unit
) : DialogFragment() {

    private var _binding: DialogLogWeightBinding? = null
    private val binding get() = _binding!!

    override fun getTheme(): Int = R.style.Theme_GymTracker_Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogLogWeightBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnSave.setOnClickListener {
            val weightStr = binding.etWeight.text?.toString()
            if (weightStr.isNullOrBlank()) {
                binding.etWeight.error = "Enter weight"
                return@setOnClickListener
            }
            val weight = weightStr.toDoubleOrNull()
            if (weight == null || weight <= 0) {
                binding.etWeight.error = "Invalid weight"
                return@setOnClickListener
            }
            val notes = binding.etNotes.text?.toString() ?: ""
            onSave(weight, notes)
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
