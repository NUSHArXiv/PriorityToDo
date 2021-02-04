package com.thepyprogrammer.prioritytodo.model

import com.thepyprogrammer.prioritytodo.ui.MainActivity
import java.time.LocalDate

data class Todo(
        var title: String,
        var priority: Float,
        var dueDate: LocalDate,
        var isChecked: Boolean = false,
        var description: String = ""
) {
    override fun toString(): String {
        val dueDateString = MainActivity.dTF.format(dueDate);
        return "$title\t$priority\t$dueDateString\t$isChecked"
    }
}