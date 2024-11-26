package com.assignment.todolistforaisteachers

import com.assignment.todolistforaisteachers.model.TaskModel

interface TaskItemClickListener
{
    fun editTaskItem(taskItem: TaskItem)
    fun editTaskFirebase(taskModel: TaskModel)
    fun completeTaskItem(taskItem: TaskItem, isChecked: Boolean)
    fun completeTaskModel(taskModel: TaskModel, isChecked: Boolean)
    fun deleteTaskItem(taskItem: TaskItem)
    fun deleteTaskFirebase(taskModel: TaskModel)
}