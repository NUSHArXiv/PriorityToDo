package com.thepyprogrammer.prioritytodo

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var todoAdapter: TodoAdapter
    private var dueDateSelected: LocalDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        todoAdapter = TodoAdapter(mutableListOf())

        rvTodoItems.adapter = todoAdapter
        rvTodoItems.layoutManager = LinearLayoutManager(this)

        priorityBar.numStars = 5

        dateSelector.setOnClickListener {

            val datePickerDialog = DatePickerDialog(this,
                { _, year, monthOfYear, dayOfMonth ->
                    dueDateSelected = LocalDate.of(year, monthOfYear, dayOfMonth)
                }, LocalDate.now().year, LocalDate.now().monthValue, LocalDate.now().dayOfMonth);
            datePickerDialog.show();
        }

        btnAddTodo.setOnClickListener {
            val todoTitle = todoTitleText.text.toString()
            val priority: Int = priorityBar.numStars

            if(todoTitle.isNotEmpty()) {
                val todo = Todo(todoTitle, priority, dueDateSelected)
                todoAdapter.addTodo(todo)
                todoTitleText.text.clear()
            }
        }
    }
}