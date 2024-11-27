package com.assignment.todolistforaisteachers
/**import android.content.Context
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.JsonFactory.getDefaultInstance
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.auth.oauth2.Credentials
import com.google.auth.http.HttpTransportFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.util.DateTime
import com.google.auth.Credentials
import java.io.InputStream
import java.io.IOException


class CalendarService (private val context: Context) {

    private val JSON_FACTORY: JsonFactory = getDefaultInstance()
    private val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()

    // Get Calendar API service
    fun getCalendarService(): Calendar {
        val credentials: Credentials = loadServiceAccountCredentials()
            .createScoped(listOf(CalendarScopes.CALENDAR))

        return Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, HttpCredentialsAdapter(credentials))
            .setApplicationName("Google Calendar API Android")
            .build()
    }

    // Load the service account credentials from the assets folder
    private fun loadServiceAccountCredentials(): ServiceAccountCredentials {
        val inputStream: InputStream = context.assets.open("service-account-file.json")
        return ServiceAccountCredentials.fromStream(inputStream)
    }

    // List events from the user's calendar
    fun listEvents() {
        try {
            val service = getCalendarService()

            // Call the Google Calendar API to get events
            val events = service.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(DateTime(System.currentTimeMillis()))
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute()

            val items = events.items
            if (items.isEmpty()) {
                println("No upcoming events found.")
            } else {
                for (event in items) {
                    println("Event: ${event.summary} (${event.start.dateTime})")
                }
            }
        } catch (e: GoogleJsonResponseException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}**/