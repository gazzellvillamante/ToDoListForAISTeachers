package com.assignment.todolistforaisteachers

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.assignment.todolistforaisteachers.databinding.ActivityMainBinding
import com.assignment.todolistforaisteachers.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText


class NewTaskSheet(var taskItem: TaskItem?) : BottomSheetDialogFragment()
{
    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var taskViewModal: TaskViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        //Add Task and Edit Task shares the same fragement
        if(taskItem != null)
        {

            binding.tvTaskTitle.text = "Edit Task"
            val editable = Editable.Factory.getInstance()
            binding.taskName.text = editable.newEditable(taskItem!!.name)
            binding.taskDescription.text = editable.newEditable(taskItem!!.desc)


        }
        else
        {
            binding.tvTaskTitle.text = "Add Task"

        }

        taskViewModal = ViewModelProvider(activity).get(TaskViewModel::class.java)
        binding.saveButton.setOnClickListener {
            saveAction()
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):View{
        binding = FragmentNewTaskSheetBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun saveAction() {
        val name = binding.taskName.text.toString()
        val desc = binding.taskDescription.text.toString()

        if (taskItem ==null){
            val newTask = TaskItem(name,desc,null)
            taskViewModal.addTaskItem(newTask)
        }
        else{
            taskViewModal.updateTaskItem(taskItem!!.id,name, desc)
        }
        binding.taskName.setText("")
        binding.taskDescription.setText("")
        dismiss()
    }




}