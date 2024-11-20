package com.assignment.todolistforaisteachers


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.Editable
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
import com.google.firebase.database.database


class NewTaskSheet(var taskItem: TaskItem?) : BottomSheetDialogFragment()
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
        if(taskItem != null)
        {
            binding.tvTaskTitle.text = "Edit Task"
            binding.saveButton.text = "Update Task"
            val editable = Editable.Factory.getInstance()
            binding.taskName.text = editable.newEditable(taskItem!!.name)
            binding.taskDescription.text = editable.newEditable(taskItem!!.desc)

        }

        else
        {
            binding.tvTaskTitle.text = "Add Task"
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

        // Add new task if taskItem is empty
        if (taskItem == null){
           val newTask = TaskItem(0,name,desc,false, 0)


            if (isDeviceOnline(requireContext())) {
                saveTaskData()  // Uses Firebase when device is online
            } else {
                db.addTask(newTask) // Uses SQLite when device is offline
            }


            Toast.makeText(context, "Task added successfully", Toast.LENGTH_SHORT).show()
        }

        else{
            // Edit existing task
            taskItem?.name = name
            taskItem?.desc = desc
            db.editTask(taskItem!!)
            Toast.makeText(context, "Task added updated", Toast.LENGTH_SHORT).show()
        }

        // Clear input fields after saving
        binding.taskName.setText("")
        binding.taskDescription.setText("")

        // Dismiss Dialog after saving
        listener?.onTaskSaved()
        dismiss()
    }

    private fun saveTaskData() {
        val taskName = binding.taskName.text.toString()
        val taskDesc = binding.taskDescription.text.toString()
        val isCompleted = false
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        val task = TaskModel(taskName, taskDesc, isCompleted, userId)

        databaseFirebase.child("task").child(userId).setValue(task)
    }

}