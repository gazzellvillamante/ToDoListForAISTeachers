package com.assignment.todolistforaisteachers

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskViewModel : ViewModel(){

    //Holds list of task items
    var taskItems = MutableLiveData<MutableList<TaskItem>>()

    //Initializes taskItems list when  ViewModel is created
    init{
        taskItems.value = mutableListOf()
    }

}