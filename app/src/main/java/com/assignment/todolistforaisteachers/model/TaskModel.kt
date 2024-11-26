package com.assignment.todolistforaisteachers.model

data class TaskModel(
    val taskId: String = "",
    var taskName: String = "",
    var taskDesc: String = "",
    var isCompleted: Boolean = false,
    val userId: String = ""
)
