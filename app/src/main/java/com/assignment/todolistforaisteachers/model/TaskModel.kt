package com.assignment.todolistforaisteachers.model

data class TaskModel(
    val taskId: String = "",
    var taskName: String = "",
    var taskDesc: String = "",
    val isCompleted: Boolean = false,
    val userId: String = ""
)
