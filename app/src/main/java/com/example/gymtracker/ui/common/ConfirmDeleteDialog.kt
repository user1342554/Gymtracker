package com.example.gymtracker.ui.common

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.gymtracker.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ConfirmDeleteDialog(
    private val onConfirm: () -> Unit,
    private val title: Int = R.string.confirm_delete_title,
    private val message: Int = R.string.confirm_delete_message
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        MaterialAlertDialogBuilder(requireContext(), R.style.Theme_GymTracker_Dialog)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.delete) { _, _ -> onConfirm() }
            .create()
}
