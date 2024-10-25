package com.assignment.todolistforaisteachers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.assignment.todolistforaisteachers.databinding.TaskItemCellBinding

class TaskItemAdapter(
    private val taskItems: MutableList<TaskItem>,
    private val clickListener: TaskItemClickListener
): RecyclerView.Adapter<TaskItemViewHolder>()

{
    /**fun removeTaskItem(taskItem: TaskItem){
        val position = taskItems.indexOf(taskItem)
        if(position != -1){
            taskItems.removeAt(position)
            notifyItemRemoved(position)
        }
    }**/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = TaskItemCellBinding.inflate(from, parent, false)

        return TaskItemViewHolder(parent.context, binding, clickListener)
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        holder.bindTaskItem(taskItems[position])
    }

    fun updateData(){
        notifyDataSetChanged()
    }

    fun removeTaskItem(position: Int){
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = taskItems.size
}