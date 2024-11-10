package com.assignment.todolistforaisteachers

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.assignment.todolistforaisteachers.databinding.ActivityToDoListBinding

class ToDoList : AppCompatActivity(), TaskItemClickListener, NewTaskSheet.OnTaskSavedListener {

    private lateinit var taskViewModel: TaskViewModel

    private lateinit var binding: ActivityToDoListBinding

    private lateinit var db: DatabaseHelper

    private lateinit var adapter: TaskItemAdapter



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityToDoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        db = DatabaseHelper(this)

        setRecyclerView()

        binding.btnBack.setOnClickListener{
            val intentBack = Intent(this, MainMenu::class.java)
            try{
                startActivity(intentBack)
            }
            catch(e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }

        binding.btnNewTask.setOnClickListener {
            try{
                NewTaskSheet(null).show(supportFragmentManager, "newTaskTag")
            }
            catch(e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setRecyclerView()
    {
        val taskList = db.showTask()

        adapter = TaskItemAdapter(taskList, this)
        binding.todoListRecyclerview.layoutManager = LinearLayoutManager(applicationContext)
        binding.todoListRecyclerview.adapter = adapter
    }

    override fun onTaskSaved(){
        val updatedTaskList = db.showTask()
        adapter.updateData(updatedTaskList)
    }

    override fun editTaskItem(taskItem: TaskItem)
    {
        try{
            NewTaskSheet(taskItem).show(supportFragmentManager, "newTasktag")
        }
        catch(e : Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun completeTaskItem(taskItem: TaskItem, isChecked: Boolean)
    {
        try {
            // Toggle the status
            val newStatus = !taskItem.isCompleted

            taskItem?.isCompleted = newStatus
            db.markAsCompleted(taskItem.id, newStatus)

            // After updating the database, refresh the RecyclerView data
            val updatedTaskList = db.showTask()

            adapter.updateData(updatedTaskList)

        }
        catch(e : Exception){
            Toast.makeText(this, "Task successfully completed", Toast.LENGTH_SHORT).show()
        }

    }

    override fun deleteTaskItem(taskItem: TaskItem) {
        val alertDialog = AlertDialog.Builder(this)

        alertDialog.setTitle("Delete Task")
        alertDialog.setMessage("Are you sure you want to delete this task?")

        alertDialog.setPositiveButton("Yes") { _, _ ->

            val deleted = db.deleteTask(taskItem.id)

            try{
                if(deleted != 0) {
                    db.deleteTask(deleted)
                    val updatedTaskList = db.showTask()

                    adapter.updateData(updatedTaskList)

                    Toast.makeText(this, "Successfuly deleted task", Toast.LENGTH_LONG).show()
                }
            }
            catch(e : Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }

        alertDialog.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialogBox = alertDialog.create()

        alertDialogBox.show()
    }

    override fun onPause(){
        super.onPause()
        Log.d("ToDoList", "Activity pause - NewTaskSheet is shown")
    }

}