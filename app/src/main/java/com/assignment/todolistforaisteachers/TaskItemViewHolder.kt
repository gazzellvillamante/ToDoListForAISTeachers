package com.assignment.todolistforaisteachers

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import androidx.core.content.ContextCompat
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

        if(taskItem.isCompleted){
            binding.cbTask.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.cbTask.buttonTintList = ColorStateList.valueOf(Color.RED)
            binding.tvName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.tvName.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
        else{
            binding.cbTask.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.cbTask.buttonTintList = ColorStateList.valueOf(Color.LTGRAY)
            binding.cbTask.setTextColor(ContextCompat.getColor(context, R.color.white))
            binding.tvName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG.inv()
            binding.tvName.setTextColor(ContextCompat.getColor(context, R.color.white))
        }

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