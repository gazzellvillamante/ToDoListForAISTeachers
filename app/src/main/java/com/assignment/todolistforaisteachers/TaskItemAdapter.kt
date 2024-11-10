package com.assignment.todolistforaisteachers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.assignment.todolistforaisteachers.databinding.TaskItemCellBinding

class TaskItemAdapter(
    private var taskItems : MutableList<TaskItem>,
    private val clickListener : TaskItemClickListener
): RecyclerView.Adapter<TaskItemViewHolder>()

{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = TaskItemCellBinding.inflate(from, parent, false)

        return TaskItemViewHolder(parent.context, binding, clickListener)
    }

    override fun getItemCount(): Int = taskItems.size

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        holder.bindTaskItem(taskItems[position])

    }

    fun updateData(newData: MutableList<TaskItem>){
        taskItems = newData
        notifyDataSetChanged()
    }




}