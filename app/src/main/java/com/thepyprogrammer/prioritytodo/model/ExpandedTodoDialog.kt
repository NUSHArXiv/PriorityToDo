package com.thepyprogrammer.prioritytodo.model

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.thepyprogrammer.prioritytodo.R

class ExpandedTodoDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.expanded_item_todo, null))
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}