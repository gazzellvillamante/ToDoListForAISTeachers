package com.assignment.todolistforaisteachers

import android.content.Context
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import java.time.LocalDate
import java.util.UUID

data class TaskItem (
    val id: Int,
    val name: String,
    val desc: String,
    val isCompleted: Boolean

    )

{
//    fun isCompleted() = isCompleted
//    fun imageResource():Int = if (isCompleted()) R.drawable.baseline_check_circle_outline_24 else R.drawable.baseline_radio_button_unchecked_24
//    fun imageColor(context: Context): Int = if(isCompleted()) red(context) else black(context)

    private fun red(context: Context) = ContextCompat.getColor(context, R.color.red)
    private fun black(context: Context) = ContextCompat.getColor(context, R.color.black)
}