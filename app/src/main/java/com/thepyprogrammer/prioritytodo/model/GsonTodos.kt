package com.thepyprogrammer.prioritytodo.model

import java.util.*

class GsonTodos internal constructor(todos: MutableList<Todo>) : ArrayList<GsonTodo>() {
    init {
        todos.forEach {
            this.add(GsonTodo(it))
        }
    }
}