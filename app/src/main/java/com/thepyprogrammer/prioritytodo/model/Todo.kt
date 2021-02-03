package com.thepyprogrammer.prioritytodo.model

import com.thepyprogrammer.prioritytodo.ui.MainActivity
import java.time.LocalDate

data class Todo(
        var title: String,
        var priority: Float,
        var dueDate: LocalDate,
        var isChecked: Boolean = false
) {
    override fun toString(): String {
        return "$title $priority " + MainActivity.dTF.format(dueDate) + " $isChecked"
    }
}