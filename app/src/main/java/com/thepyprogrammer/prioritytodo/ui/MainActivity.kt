package com.thepyprogrammer.prioritytodo.ui

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.thepyprogrammer.prioritytodo.R
import com.thepyprogrammer.prioritytodo.model.Todo
import com.thepyprogrammer.prioritytodo.model.TodoAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.PrintWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var todoAdapter: TodoAdapter
    private var dueDateSelected: LocalDate = LocalDate.now()

    companion object {
        val dTF: DateTimeFormatter = DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd/MM/yyyy").toFormatter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        todoAdapter = TodoAdapter()

        rvTodoItems.adapter = todoAdapter
        rvTodoItems.layoutManager = LinearLayoutManager(this)

        priorityBar.rating = 2.5F

        dateSelector.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this,
                { _, year, monthOfYear, dayOfMonth ->
                    dueDateSelected = LocalDate.of(year, monthOfYear+1, dayOfMonth)
                    dateSelector.text = dTF.format(dueDateSelected)
                }, dueDateSelected.year, dueDateSelected.monthValue - 1, dueDateSelected.dayOfMonth)
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000;
            datePickerDialog.show()
        }

        btnAddTodo.setOnClickListener {
            val todoTitle = todoTitleText.text.toString()
            val priority: Float = priorityBar.rating

            if(todoTitle.isNotEmpty()) {
                val todo = Todo(todoTitle, priority, dueDateSelected)
                todoAdapter.addTodo(todo)
                todoTitleText.text.clear()
            }
        }
    }

    override fun onDestroy() {
        val dbFile = File(filesDir.path.toString() + "/raw/todo.txt")
        val pw = PrintWriter(dbFile)
        todoAdapter.todos.forEach {
            pw.println(it)
        }
        pw.close()
        super.onDestroy()
    }
}