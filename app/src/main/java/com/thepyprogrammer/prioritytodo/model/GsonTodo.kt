package com.thepyprogrammer.prioritytodo.model

import com.google.gson.annotations.SerializedName
import com.thepyprogrammer.prioritytodo.ui.MainActivity
import java.time.LocalDate

data class GsonTodo(
    @SerializedName("title") var title: String,
    @SerializedName("priority") var priority: String,
    @SerializedName("dueDate") var dueDate: String,
    @SerializedName("isChecked") var isChecked: String,
    @SerializedName("description") var description: String = ""
) {
    constructor(todo: Todo) : this(
        todo.title,
        "${todo.priority}",
        MainActivity.dTF.format(todo.dueDate),
        "${todo.isChecked}",
        todo.description
    )

    fun toTodo(): Todo {
        return Todo(
            title,
            priority.toFloat(),
            LocalDate.parse(dueDate, MainActivity.dTF),
            isChecked.toBoolean(),
            description
        )
    }
}