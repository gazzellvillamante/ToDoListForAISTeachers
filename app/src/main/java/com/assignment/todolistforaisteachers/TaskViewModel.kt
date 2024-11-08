package com.assignment.todolistforaisteachers

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.util.UUID

class TaskViewModel : ViewModel(){

    //Holds list of task items
    var taskItems = MutableLiveData<MutableList<TaskItem>>()

    private lateinit var db: DatabaseHelper

    //Initializes taskItems list when  ViewModel is created
    init{
        //taskItems.value = db.showTask()
        taskItems.value = mutableListOf()
    }



    //Function for adding task item to the list
   /** @SuppressLint("NullSafeMutableLiveData")
    fun addTaskItem(newTask: TaskItem){

        val list = taskItems.value
        list!!.add(newTask)
        //
        taskItems.postValue(list)
    }**/

    /**@SuppressLint("NullSafeMutableLiveData")
    fun updateTaskItem(id: UUID, name: String, desc: String){
        val list = taskItems.value
        val task = list!!.find { it.id == id }!!
        task.name = name
        task.desc = desc
        taskItems.postValue(list)
    }**/

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NullSafeMutableLiveData")
    fun setCompleted(taskItem: TaskItem){
        val list = taskItems.value
        val task = list!!.find { it.id == taskItem.id }!!
        if(task.isCompleted){
            System.out.println("Completed")
        }
        taskItems.postValue(list)
    }


    /**@SuppressLint("NullSafeMutableLiveData")
    fun deleteTask(taskItem: TaskItem) {
        val list = taskItems.value
        val taskToRemove = list!!.find { it.id == taskItem.id }


        if (taskToRemove != null) {
            list.remove(taskToRemove)
            taskItems.postValue(list)
        }
    }**/

}