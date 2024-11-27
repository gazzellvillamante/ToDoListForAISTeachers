/**package com.assignment.todolistforaisteachers


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import kotlinx.coroutines.*

class GoogleCalendar : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_calendar)

        // Start the API call in a background thread
        CoroutineScope(Dispatchers.IO).launch {
            calendarService.listEvents()
        }
    }
}**/