package com.assignment.todolistforaisteachers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.assignment.todolistforaisteachers.databinding.ActivityBrowseWebBinding

class BrowseWeb : AppCompatActivity() {

    lateinit var binding : ActivityBrowseWebBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrowseWebBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnBrowseWeb.setOnClickListener{
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"))

            try{
                startActivity(intent)
            }
            catch(e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }

        binding.btnBack.setOnClickListener{
            val intentBack = Intent(this, MainMenu::class.java)

            try{
                startActivity(intentBack)
            }
            catch(e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }

    }
}