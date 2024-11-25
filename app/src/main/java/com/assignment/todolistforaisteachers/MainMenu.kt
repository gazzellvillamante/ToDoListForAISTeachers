package com.assignment.todolistforaisteachers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainMenu : AppCompatActivity() {

    //private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu)


        val navView : NavigationView = findViewById(R.id.nav_view)

        //Setting up intent for the different classes in the app
        val intentGalleryView = Intent(this, GalleryView::class.java)
        val intentMapsView = Intent(this, MapsActivity::class.java)
        val intentDialPhone = Intent(this, DialAPhone::class.java)
        val sendEmail = Intent(this, SendEmail::class.java)
        val browseWeb = Intent(this, BrowseWeb::class.java)
        val logout = Intent(this, MainActivity::class.java)
        val toDoList = Intent(this, ToDoList::class.java)
        val intentVideoPlayer = Intent(this, VideoPlayer::class.java)


        navView.setNavigationItemSelectedListener {

            when(it.itemId){
                R.id.nav_taskMaster -> startActivity(toDoList)

                R.id.nav_gallery -> startActivity(intentGalleryView)

                R.id.nav_map ->  startActivity(intentMapsView)

                R.id.nav_phone -> startActivity(intentDialPhone)

                R.id.nav_sendEmail -> startActivity(sendEmail)

                R.id.nav_browseWeb -> startActivity(browseWeb)

                R.id.nav_videoPlayer -> startActivity(intentVideoPlayer)

                R.id.nav_logout -> startActivity(logout)

            }
            true
        }

    }
}