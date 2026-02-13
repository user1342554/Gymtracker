package com.example.gymtracker.ui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.gymtracker.R
import com.example.gymtracker.data.entity.Exercise
import com.example.gymtracker.databinding.DialogAddEditExerciseBinding

class AddEditExerciseDialogFragment(
    private val existingExercise: Exercise? = null,
    private val onSave: (name: String, sets: Int, reps: Int, rest: Int, notes: String) -> Unit
) : DialogFragment() {

    private var _binding: DialogAddEditExerciseBinding? = null
    private val binding get() = _binding!!

    override fun getTheme(): Int = R.style.Theme_GymTracker_Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogAddEditExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        existingExercise?.let {
            binding.tvDialogTitle.setText(R.string.edit_exercise)
            binding.etExerciseName.setText(it.name)
            binding.etSets.setText(it.targetSets.toString())
            binding.etReps.setText(it.targetReps.toString())
            binding.etRest.setText(it.restSeconds.toString())
            binding.etNotes.setText(it.notes)
        }

        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnSave.setOnClickListener {
            val name = binding.etExerciseName.text?.toString()?.trim()
            if (name.isNullOrBlank()) {
                binding.etExerciseName.error = "Enter exercise name"
                return@setOnClickListener
            }
            val sets = binding.etSets.text?.toString()?.toIntOrNull() ?: 3
            val reps = binding.etReps.text?.toString()?.toIntOrNull() ?: 10
            val rest = binding.etRest.text?.toString()?.toIntOrNull() ?: 60
            val notes = binding.etNotes.text?.toString()?.trim() ?: ""
            onSave(name, sets, reps, rest, notes)
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
