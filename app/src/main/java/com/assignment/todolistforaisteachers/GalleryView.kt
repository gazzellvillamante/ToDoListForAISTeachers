package com.assignment.todolistforaisteachers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.assignment.todolistforaisteachers.databinding.ActivityDialAphoneBinding
import com.assignment.todolistforaisteachers.databinding.ActivityGalleryViewBinding

class GalleryView : AppCompatActivity() {

    private lateinit var button: Button
    private lateinit var backButton: ImageView
    private lateinit var imageView: ImageView


    // Declare the launcher at the class level
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the selected image URI
        uri?.let {
            imageView.setImageURI(uri) // Set the selected image to the ImageView
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_view)

        button = findViewById(R.id.btnChoose)
        backButton = findViewById(R.id.btnBack)
        imageView = findViewById(R.id.saveImage)


        button.setOnClickListener{
            try{
                pickImageGallery()
            }
            catch(e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }

        backButton.setOnClickListener{
            val intentBack = Intent(this, MainMenu::class.java)
            try{
                startActivity(intentBack)
            }
            catch(e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun pickImageGallery() {

        // Launch the image picker with the "image/*" MIME type
        pickImageLauncher.launch("image/*")
    }


}