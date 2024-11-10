package com.assignment.todolistforaisteachers

import android.content.Context
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.assignment.todolistforaisteachers.databinding.TaskItemCellBinding

class TaskItemViewHolder(
    private val context: Context,
    private val binding: TaskItemCellBinding,
    private val clickListener: TaskItemClickListener
): RecyclerView.ViewHolder(binding.root)
{

    fun bindTaskItem(taskItem: TaskItem)
    {
        binding.tvName.text = taskItem.name
        binding.cbTask.isChecked = taskItem.isCompleted

        binding.taskCellContainer.setOnClickListener{
            clickListener.editTaskItem(taskItem)
        }

        binding.btnDelete.setOnClickListener{
            clickListener.deleteTaskItem(taskItem)
        }

        // Handle checkbox click
        binding.cbTask.setOnCheckedChangeListener { _, isChecked ->
            clickListener.completeTaskItem(taskItem, true)
        }
    }


}