package com.assignment.todolistforaisteachers

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.assignment.todolistforaisteachers.databinding.ActivityDialAphoneBinding
import com.assignment.todolistforaisteachers.databinding.ActivityVideoPlayerBinding

class VideoPlayer : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding

    private lateinit var videoView: VideoView

    var videoUrl =
        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videoView = findViewById(R.id.vvPlayVideo);

        val uri: Uri = Uri.parse(videoUrl)

        // Set video uri for video view.
        videoView.setVideoURI(uri)

        val mediaController = MediaController(this)

        // Set anchor view for media controller.
        mediaController.setAnchorView(videoView)

        // Set media player for media controller.
        mediaController.setMediaPlayer(videoView)

        // Controller for video view.
        videoView.setMediaController(mediaController)

        // Starts video view.
        videoView.start()
    }
}