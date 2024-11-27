package com.assignment.todolistforaisteachers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.assignment.todolistforaisteachers.databinding.TaskItemCellBinding
import com.assignment.todolistforaisteachers.model.TaskModel


class TaskModelAdapter(
    private var taskItems : MutableList<TaskModel>,
    private val clickListener : TaskItemClickListener,
): RecyclerView.Adapter<TaskModelViewHolder>()

{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskModelViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = TaskItemCellBinding.inflate(from, parent, false)

        return TaskModelViewHolder(parent.context, binding, clickListener)
    }

    override fun getItemCount(): Int = taskItems.size

    override fun onBindViewHolder(holder: TaskModelViewHolder, position: Int) {
        holder.bindTaskItem(taskItems[position])

    }

    fun updateData(newData: MutableList<TaskModel>){
        taskItems = newData
        notifyDataSetChanged()
    }

}