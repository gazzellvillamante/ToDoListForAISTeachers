package com.assignment.todolistforaisteachers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.assignment.todolistforaisteachers.databinding.ActivityDialAphoneBinding

class DialAPhone : AppCompatActivity() {

    private lateinit var binding: ActivityDialAphoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =ActivityDialAphoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCall.setOnClickListener{
            val num = binding.etPhone.text.toString()

            // Check if Edit Text for Phone number is not empty
            if(num.isNotEmpty()){
                //Set up intent for dialing a phone number
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:"+ num)

                try {
                    startActivity(callIntent)
                }
                catch( e : Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(this, "Please enter a mobile number", Toast.LENGTH_LONG).show()
            }
        }
    }
}