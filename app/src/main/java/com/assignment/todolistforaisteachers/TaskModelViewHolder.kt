package com.assignment.todolistforaisteachers

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.assignment.todolistforaisteachers.databinding.TaskItemCellBinding
import com.assignment.todolistforaisteachers.model.TaskModel

class TaskModelViewHolder(
    private val context: Context,
    private val binding: TaskItemCellBinding,
    private val clickListener: TaskItemClickListener
): RecyclerView.ViewHolder(binding.root)
{

    fun bindTaskItem(taskModel: TaskModel)
    {
        binding.tvName.text = taskModel.taskName
        binding.cbTask.isChecked = taskModel.isCompleted == true

        if(taskModel.isCompleted){
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
            clickListener.editTaskFirebase(taskModel)
        }

        binding.btnDelete.setOnClickListener{
            clickListener.deleteTaskFirebase(taskModel)
        }

        // Handle checkbox click
        binding.cbTask.setOnCheckedChangeListener { _, isChecked ->

            taskModel.isCompleted = isChecked
            clickListener.completeTaskModel(taskModel, true)

            if(taskModel.isCompleted){
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

        }
    }


}