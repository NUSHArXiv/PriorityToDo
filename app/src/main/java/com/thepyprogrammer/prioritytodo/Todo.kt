package com.thepyprogrammer.prioritytodo

import java.time.LocalDate

data class Todo(
    val title: String,
    var priority: Float,
    var dueDate: LocalDate,
    var isChecked: Boolean = false
)