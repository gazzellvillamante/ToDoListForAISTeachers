package com.assignment.todolistforaisteachers

interface TaskItemClickListener
{
    fun editTaskItem(taskItem: TaskItem)
    fun completeTaskItem(taskItem: TaskItem, isChecked: Boolean)
    fun deleteTaskItem(taskItem: TaskItem)
}