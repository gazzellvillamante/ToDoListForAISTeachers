package com.assignment.todolistforaisteachers

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.assignment.todolistforaisteachers.databinding.ActivityGalleryViewBinding

class GalleryView : AppCompatActivity() {

    private lateinit var button: Button
    private lateinit var imageView: ImageView

    //private lateinit var binding: ActivityGalleryViewBinding

    companion object{

        val IMAGE_REQUEST_CODE = 100

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_view)

        button = findViewById(R.id.btnChoose)
        imageView = findViewById(R.id.saveImage)

        button.setOnClickListener{
            pickImageGallery()
        }
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            imageView.setImageURI(data?.data)
        }
    }
}