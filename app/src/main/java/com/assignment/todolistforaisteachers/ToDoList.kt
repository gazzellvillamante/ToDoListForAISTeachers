package com.assignment.todolistforaisteachers

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.assignment.todolistforaisteachers.databinding.ActivityToDoListBinding

class ToDoList : AppCompatActivity(), TaskItemClickListener {

    private lateinit var taskViewModel: TaskViewModel

    private lateinit var binding: ActivityToDoListBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityToDoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        binding.btnNewTask.setOnClickListener {
            NewTaskSheet(null).show(supportFragmentManager, "newTaskTag")
        }

        setRecyclerView()

    }

    private fun setRecyclerView()
    {
        val mainActivity = this
        taskViewModel.taskItems.observe(this){
            binding.todoListRecyclerview.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = TaskItemAdapter(it, mainActivity)

            }
        }
    }

    override fun editTaskItem(taskItem: TaskItem)
    {
        NewTaskSheet(taskItem).show(supportFragmentManager, "newTasktag")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun completeTaskItem(taskItem: TaskItem)
    {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Complete Task")
        alertDialog.setMessage("Are you sure you want to complete this task?")

        //Got this Alert Dialog code from https://developer.android.com/develop/ui/views/components/dialogs
        alertDialog.setPositiveButton("Yes") { _, _ ->
            taskViewModel.setCompleted(taskItem)
        }

        alertDialog.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialogBox = alertDialog.create()

        alertDialogBox.show()
    }

    override fun deleteTaskItem(taskItem: TaskItem) {
        val alertDialog = AlertDialog.Builder(this)

        alertDialog.setTitle("Delete Task")
        alertDialog.setMessage("Are you sure you want to delete this task?")

        //Got this Alert Dialog code from https://developer.android.com/develop/ui/views/components/dialogs
        alertDialog.setPositiveButton("Yes") { _, _ ->
            taskViewModel.deleteTask(taskItem)
        }

        alertDialog.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialogBox = alertDialog.create()

        alertDialogBox.show()
    }

}