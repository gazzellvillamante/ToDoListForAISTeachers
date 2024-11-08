package com.assignment.todolistforaisteachers

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.assignment.todolistforaisteachers.databinding.ActivitySendEmailBinding
import com.assignment.todolistforaisteachers.databinding.ActivityToDoListBinding

class SendEmail : AppCompatActivity() {

    private lateinit var binding: ActivitySendEmailBinding

    @SuppressLint("IntentReset")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySendEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSend.setOnClickListener{
            var recipient = binding.etRecipientEmail.text.toString().trim()
            var subject = binding.etSubject.text.toString().trim()
            var message = binding.etMessage.text.toString().trim()

            var intent = Intent(Intent.ACTION_SENDTO).apply {

                data = Uri.parse("mailto:")

                putExtra(Intent.EXTRA_EMAIL, recipient)
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, message)
            }

            try {
                startActivity(intent)
            } catch (e: Exception) {
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