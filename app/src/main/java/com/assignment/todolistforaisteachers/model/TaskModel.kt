package com.assignment.todolistforaisteachers.model

data class TaskModel(
    val taskName: String? = null,
    val taskDesc: String? = null,
    val isCompleted: Boolean? = null,
    val userId: String? = null
)
