package com.thepyprogrammer.prioritytodo.model

import java.io.InputStream
import java.util.ArrayList

class GsonTodos internal constructor(todos: MutableList<Todo>) : ArrayList<GsonTodo>() {
    init {
        todos.forEach {
            this.add(GsonTodo(it))
        }
    }
}