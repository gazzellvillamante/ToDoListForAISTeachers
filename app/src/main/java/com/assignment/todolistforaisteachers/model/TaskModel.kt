package com.assignment.todolistforaisteachers.model

data class TaskModel(
    val taskId: String = "",
    val taskName: String = "",
    val taskDesc: String = "",
    val isCompleted: Boolean = false,
    val userId: String = ""
)
