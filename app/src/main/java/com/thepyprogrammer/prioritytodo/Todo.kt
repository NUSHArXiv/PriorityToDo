package com.thepyprogrammer.prioritytodo

import java.time.LocalDate

data class Todo(
    val title: String,
    val priority: Int,
    val dueDate: LocalDate,
    var isChecked: Boolean = false
)