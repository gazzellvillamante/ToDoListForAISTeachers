package com.assignment.todolistforaisteachers

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.assignment.todolistforaisteachers.databinding.ActivityToDoListBinding
import com.assignment.todolistforaisteachers.model.TaskModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ToDoList : AppCompatActivity(), TaskItemClickListener, NewTaskSheet.OnTaskSavedListener {

    private lateinit var binding: ActivityToDoListBinding

    private lateinit var db: DatabaseHelper

    private lateinit var adapter: TaskItemAdapter

    private lateinit var databaseFirebase: DatabaseReference

    private lateinit var firebaseModel: TaskModelAdapter




    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityToDoListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = DatabaseHelper(this)
        databaseFirebase = FirebaseDatabase.getInstance().getReference("task")


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
                NewTaskSheet(null, null).show(supportFragmentManager, "newTaskTag")
            }
            catch(e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }

        binding.btnSearch.setOnClickListener {
            val searchString = binding.etSearch.text.toString().trim()
            if(searchString.isNotEmpty()){
                try{
                    if(isDeviceOnline(this)){
                        searchTaskFirebase(searchString)
                    } else {
                        searchTask(searchString)
                    }

                } catch(e:Exception){
                    Toast.makeText(this,e.message, Toast.LENGTH_LONG).show()
                }
            } else {

                setRecyclerView()
            }
        }
    }

    private fun setRecyclerView()
    {
        val context = this
        if(isDeviceOnline(context)){
            showTaskFirebase()
            firebaseModel = TaskModelAdapter(mutableListOf(), this)
            binding.todoListRecyclerview.layoutManager = LinearLayoutManager(applicationContext)
            binding.todoListRecyclerview.adapter = firebaseModel
        } else {
            val taskList = db.showTask()

            adapter = TaskItemAdapter(taskList, this)
            binding.todoListRecyclerview.layoutManager = LinearLayoutManager(applicationContext)
            binding.todoListRecyclerview.adapter = adapter
        }

    }

    override fun onTaskSaved(){
        val context = this
        if(isDeviceOnline(context)) {
            showTaskFirebase()
            firebaseModel = TaskModelAdapter(mutableListOf(), this)
            binding.todoListRecyclerview.layoutManager = LinearLayoutManager(applicationContext)
            binding.todoListRecyclerview.adapter = firebaseModel
        } else {
            val updatedTaskList = db.showTask()
            adapter.updateData(updatedTaskList)
        }
    }

    override fun editTaskItem(taskItem: TaskItem)
    {
        try{
            NewTaskSheet(taskItem, null).show(supportFragmentManager, "newTasktag")
        }
        catch(e : Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun editTaskFirebase(taskModel: TaskModel){
        try{
            NewTaskSheet(null, taskModel).show(supportFragmentManager, "newTasktag")
        } catch(e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
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

    override fun completeTaskModel(taskModel: TaskModel, isChecked: Boolean){
        try{

            val userId = FirebaseAuth.getInstance().currentUser?.uid

            if(userId == null){
                Toast.makeText(this, "User is not logged in.", Toast.LENGTH_SHORT).show()
                return
            }

            if(taskModel.taskId.isEmpty()){
                Toast.makeText(this, "Invalid Task ID.", Toast.LENGTH_SHORT).show()
                return
            }

            val taskRef = FirebaseDatabase.getInstance().getReference("task/$userId/${taskModel.taskId}")
            taskRef.child("completed").setValue(isChecked)

        }catch(e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
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

    override fun deleteTaskFirebase(taskModel: TaskModel){
        val alertDialog = AlertDialog.Builder(this)

        alertDialog.setTitle("Delete Task")
        alertDialog.setMessage("Are you sure you want to delete this task?")

        alertDialog.setPositiveButton("Yes") { _, _ ->
            try{
                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                if (userId == null) {
                    Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                }

                if (taskModel.taskId.isEmpty()) {
                    Toast.makeText(this, "Invalid Task ID", Toast.LENGTH_SHORT).show()
                }

                val taskRef = FirebaseDatabase.getInstance().getReference("task/$userId/${taskModel.taskId}")
                taskRef.removeValue()

                showTaskFirebase()
                firebaseModel = TaskModelAdapter(mutableListOf(), this)
                binding.todoListRecyclerview.layoutManager = LinearLayoutManager(applicationContext)
                binding.todoListRecyclerview.adapter = firebaseModel

            } catch(e: Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
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

    private fun searchTask(searchString: String) {
        val tasks = db.searchTask(searchString)
        if (tasks.isNotEmpty()) {
            adapter.updateData(tasks)
        } else {
            Toast.makeText(this, "No task found", Toast.LENGTH_SHORT).show()
        }
    }

    fun isDeviceOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || activeNetwork.hasTransport(
            NetworkCapabilities.TRANSPORT_CELLULAR)

    }

    private fun showTaskFirebase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "No user is logged in.", Toast.LENGTH_SHORT).show()
            return
        }

        databaseFirebase.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val taskList = mutableListOf<TaskModel>()
                for (taskSnapshot in snapshot.children) {
                    val task = taskSnapshot.getValue(TaskModel::class.java)
                    if (task != null) {
                        taskList.add(task)
                    }
                }

                if (taskList.isNotEmpty()) {
                    firebaseModel.updateData(taskList)
                    Log.d("Firebase", "Fetched ${taskList.size} tasks for userId: $userId")
                } else {
                    Toast.makeText(this@ToDoList, "No tasks found", Toast.LENGTH_SHORT).show()
                    Log.d("Firebase", "No tasks found for userId: $userId")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ToDoList,
                    "Failed to fetch tasks: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
                Log.e("Firebase", "Database error: ${error.message}")
            }
        })

    }

    private fun searchTaskFirebase(searchString: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "No user is logged in.", Toast.LENGTH_SHORT).show()
            return
        }

        databaseFirebase.child(userId)
            .orderByChild("taskName")
            .startAt(searchString)
            .endAt(searchString + "\uf8ff") // Ensures case-insensitive prefix search
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val taskList = mutableListOf<TaskModel>()
                    for (taskSnapshot in snapshot.children) {
                        val task = taskSnapshot.getValue(TaskModel::class.java)
                        if (task != null) {
                            taskList.add(task)
                        }
                    }

                    if (taskList.isNotEmpty()) {
                        firebaseModel.updateData(taskList)
                        Toast.makeText(this@ToDoList, "Found ${taskList.size} tasks", Toast.LENGTH_SHORT).show()
                        Log.d("Firebase", "Search returned ${taskList.size} results.")
                    } else {
                        Toast.makeText(this@ToDoList, "No tasks found", Toast.LENGTH_SHORT).show()
                        Log.d("Firebase", "No tasks matched the search query.")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@ToDoList,
                        "Search failed: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("Firebase", "Search error: ${error.message}")
                }
            })
    }


}