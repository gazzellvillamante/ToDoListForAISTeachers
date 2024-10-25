package com.assignment.todolistforaisteachers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
            startActivity(intent)
        }

    }
}