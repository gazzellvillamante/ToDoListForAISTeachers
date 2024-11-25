package com.assignment.todolistforaisteachers


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.assignment.todolistforaisteachers.databinding.FragmentNewTaskSheetBinding
import com.assignment.todolistforaisteachers.model.TaskModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database


class NewTaskSheet(var taskItem: TaskItem?, var taskModel: TaskModel?) : BottomSheetDialogFragment()
{

    interface OnTaskSavedListener {
        fun onTaskSaved()
    }

    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var db: DatabaseHelper
    private var listener: OnTaskSavedListener? = null
    private lateinit var authFirebase: FirebaseAuth
    private lateinit var databaseFirebase: DatabaseReference


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        // Initialization of database
        db = DatabaseHelper(activity)

        //initialize Firebase
        authFirebase = Firebase.auth
        //initialize Firebase Database
        databaseFirebase = Firebase.database.reference


        //Add Task and Edit Task shares the same fragement
        if(isDeviceOnline(requireContext())){
            if(taskModel != null){
                binding.tvTaskTitle.text = "Edit Task"
                binding.saveButton.text = "Update Task"
                val editable = Editable.Factory.getInstance()
                binding.taskName.text = editable.newEditable(taskModel!!.taskName)
                binding.taskDescription.text = editable.newEditable(taskModel!!.taskDesc)
            } else {
                binding.tvTaskTitle.text = "Add Task"
            }
        } else {
            if(taskItem != null) {
                binding.tvTaskTitle.text = "Edit Task"
                binding.saveButton.text = "Update Task"
                val editable = Editable.Factory.getInstance()
                binding.taskName.text = editable.newEditable(taskItem!!.name)
                binding.taskDescription.text = editable.newEditable(taskItem!!.desc)

            } else {
                binding.tvTaskTitle.text = "Add Task"
            }
        }

        binding.saveButton.setOnClickListener {
            saveAction()
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):View{
        binding = FragmentNewTaskSheetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onAttach(context: Context){
        super.onAttach(context)
        if(context is OnTaskSavedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement onTaskSavedListener")
        }
    }

    override fun onDetach(){
        super.onDetach()
        listener = null
    }

    //Check if device is online
    fun isDeviceOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || activeNetwork.hasTransport(
            NetworkCapabilities.TRANSPORT_CELLULAR)

    }

    private fun saveAction() {
        // Get the task name and description
        val name = binding.taskName.text.toString()
        val desc = binding.taskDescription.text.toString()

        // Input validation (make sure name and description aren't empty)
        if (name.isEmpty() || desc.isEmpty()) {
            Toast.makeText(context, "Please fill in both name and description", Toast.LENGTH_SHORT).show()
            return
        }

        // Add new task if taskItem or taskModel is empty
        if(isDeviceOnline(requireContext())){
            if(taskModel == null) {
                val newTask = TaskModel("", name, desc,false, "")
                saveTaskData(newTask)
                Toast.makeText(context, "Task added successfully", Toast.LENGTH_SHORT).show()
            }else {
                //update existing task in firebase
                taskModel?.taskName = name
                taskModel?.taskDesc = desc
                updateTaskFirebase(taskModel!!)
                Toast.makeText(context, "Task updated", Toast.LENGTH_SHORT).show()
            }
        } else {
            if(taskItem == null) {
                val newTask = TaskItem(0, name,desc, false, 0)
                db.addTask(newTask)
                Toast.makeText(context, "Task added successfully", Toast.LENGTH_SHORT).show()
            } else {
                //Edit existing Task
                taskItem?.name = name
                taskItem?.desc = desc
                db.editTask(taskItem!!)
                Toast.makeText(context, "Task updated", Toast.LENGTH_SHORT).show()
            }
        }

        // Clear input fields after saving
        binding.taskName.setText("")
        binding.taskDescription.setText("")

        // Dismiss Dialog after saving
        listener?.onTaskSaved()
        dismiss()
    }

    private fun saveTaskData(newTask: TaskModel) {
        // Retrieve the current user's ID
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Ensure the user is logged in
        if (userId == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Generate a unique key for the new task
        val taskKey = databaseFirebase.child("task").child(userId).push().key

        if (taskKey == null) {
            Toast.makeText(context, "Failed to generate task key", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a task object with the generated task ID and user ID
        val task = TaskModel(
            taskId = taskKey,
            taskName = newTask.taskName,
            taskDesc = newTask.taskDesc,
            isCompleted = newTask.isCompleted,
            userId = userId
        )


        // Save the task under the user's node using the unique key
        databaseFirebase.child("task").child(userId).child(taskKey).setValue(task)
    }

    private fun updateTaskFirebase(updatedTask: TaskModel) {
        //retrieve userId from logged in user
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        if(updatedTask.taskId.isNullOrEmpty()){
            Toast.makeText(context, "Invalid Task ID", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("Firebase", "Updating task at path: task/$userId/${updatedTask.taskId}")
        Log.d("Firebase", "Task data: $updatedTask")


        //Update the existing task in realtime database
        databaseFirebase.child("task").child(userId).child(updatedTask.taskId).setValue(updatedTask)

    }

}