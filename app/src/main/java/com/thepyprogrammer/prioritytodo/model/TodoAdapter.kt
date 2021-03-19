package com.thepyprogrammer.prioritytodo.model

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.thepyprogrammer.prioritytodo.R
import com.thepyprogrammer.prioritytodo.ui.MainActivity
import kotlinx.android.synthetic.main.item_todo.view.*
import java.time.LocalDate


class TodoAdapter(
    private val activity: MainActivity,
    val todos: MutableList<Todo> = mutableListOf()
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    var recentlyDeleted: Todo? = null
    var recentlyDeletedPosition: Int = -1

    class TodoViewHolder(itemView: CardView) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_todo,
                parent,
                false
            ) as CardView
        )
    }

    fun addTodo(todo: Todo) {
        todos.add(todo)
        notifyItemInserted(todos.size - 1)
        sort()
        notifyDataSetChanged()
        activity.updateFile()

    }

    fun sort() {
        todos.sortWith { p1, p2 ->
            when {
                p1.dueDate > p2.dueDate -> 1
                p1.dueDate == p2.dueDate -> if (p1.priority > p2.priority) -1 else if (p1.priority < p2.priority) 1 else p1.title.compareTo(
                    p2.title
                )
                else -> -1
            }
        }
    }

    private fun toggleStrikeThrough(tvTodoTitle: TextView, isChecked: Boolean, dateView: Button) {
        if (isChecked) {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG

        } else {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
        dateView.isEnabled = !isChecked
        tvTodoTitle.isEnabled = !isChecked
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val curTodo = todos[position]
        holder.itemView.apply {
            todoTitleView.setText(curTodo.title)
            cbDone.isChecked = curTodo.isChecked
            dateView.text = MainActivity.dTF.format(curTodo.dueDate)
            priorityBar.rating = curTodo.priority
            toggleStrikeThrough(todoTitleView, curTodo.isChecked, dateView)


            cbDone.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(todoTitleView, isChecked, dateView)
                curTodo.isChecked = !curTodo.isChecked
                sort()
                activity.updateFile()
            }

            holder.itemView.setOnClickListener {
                showExpandedTodoDialog(curTodo, context)

            }

            dateView.setOnClickListener {
                val datePickerDialog = DatePickerDialog(
                    context,
                    { _, year, monthOfYear, dayOfMonth ->
                        curTodo.dueDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                        sort()
                        // notifyItemChanged(position)
                        notifyDataSetChanged()
                        activity.updateFile()
                    },
                    curTodo.dueDate.year,
                    curTodo.dueDate.monthValue - 1,
                    curTodo.dueDate.dayOfMonth
                )
                datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
                datePickerDialog.show()
            }

            todoTitleView.setOnKeyListener(object : View.OnKeyListener {
                override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                    if (keyCode == KeyEvent.KEYCODE_ENTER && todoTitleView.text.toString()
                            .isNotEmpty()
                    ) {
                        curTodo.title = todoTitleView.text.toString()
                        sort()
                        // notifyItemChanged(position)
                        notifyDataSetChanged()
                        activity.updateFile()
                        return true
                    }
                    return false
                }
            })
        }
    }


    fun showExpandedTodoDialog(todo: Todo, context: Context) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Edit Todo")
        // set the custom layout
        val customLayout: View = LayoutInflater.from(activity.applicationContext).inflate(
            R.layout.expanded_item_todo,
            null
        )
        builder.setView(customLayout)
        // create and show the alert dialog

        val customPriorityBar = customLayout.findViewById<RatingBar>(R.id.priorityBar)
        val todoTitleView = customLayout.findViewById<EditText>(R.id.todoTitleView)
        val dateView = customLayout.findViewById<Button>(R.id.dateView)
        val cbDone = customLayout.findViewById<CheckBox>(R.id.cbDone)
        val description = customLayout.findViewById<EditText>(R.id.detailsText)

        todoTitleView.setText(todo.title)
        cbDone.isChecked = todo.isChecked
        dateView.text = MainActivity.dTF.format(todo.dueDate)
        customPriorityBar.rating = todo.priority
        description.setText(todo.description)
        toggleStrikeThrough(todoTitleView, todo.isChecked, dateView)

        cbDone.setOnCheckedChangeListener { _, isChecked ->
            toggleStrikeThrough(todoTitleView, isChecked, dateView)
            todo.isChecked = !todo.isChecked
        }

        dateView.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, monthOfYear, dayOfMonth ->
                    todo.dueDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                },
                todo.dueDate.year,
                todo.dueDate.monthValue - 1,
                todo.dueDate.dayOfMonth
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
        }

        todoTitleView.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (keyCode == KeyEvent.KEYCODE_ENTER && todoTitleView.text.toString()
                        .isNotEmpty()
                ) {
                    todo.title = todoTitleView.text.toString()
                    return true
                }
                return false
            }
        })

        builder.setPositiveButton(
            "OK"
        ) { _, _ ->
            val priority: Float = customPriorityBar.rating
            todo.description = description.text.toString()
            todo.priority = priority
            sort()
            notifyDataSetChanged()
            activity.updateFile()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun deleteItem(position: Int) {
        todos.removeAt(position)
        notifyDataSetChanged()
        val view: View = activity.findViewById(R.id.home)
        val snackbar: Snackbar = Snackbar.make(
            view, "Task Has Been Deleted",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("UNDO") { undoDelete() }
        snackbar.show()
        activity.updateFile()
    }

    private fun undoDelete() {
        recentlyDeleted?.let {
            todos.add(
                recentlyDeletedPosition,
                it
            )
            notifyItemInserted(recentlyDeletedPosition)
            val view: View = activity.findViewById(R.id.home)
            val snackbar: Snackbar = Snackbar.make(
                view, "Task Has Been Restored",
                Snackbar.LENGTH_SHORT
            )
            snackbar.show()
        }
    }

    override fun getItemCount(): Int {
        return todos.size
    }
}


















