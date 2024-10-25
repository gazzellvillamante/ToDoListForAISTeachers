package com.assignment.todolistforaisteachers

import android.os.Bundle
import android.widget.Toast
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.assignment.todolistforaisteachers.databinding.ActivitySignUpBinding

class SignUp : AppCompatActivity() {

    private lateinit var databaseHelper : DatabaseHelper

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        binding.btnSignUp.setOnClickListener{
            val username = binding.txtUsername.text.toString()
            val password = binding.txtPassword.text.toString()
            val confirmpassword = binding.txtConfirmPassword.text.toString()

            signupDatabase(username,password,confirmpassword)
        }

    }


    private fun signupDatabase(username: String, password: String, confirmpassword: String){
        val insertRowId = databaseHelper.insertUser(username, password,confirmpassword)
        if(insertRowId != -1L && password == confirmpassword){
            Toast.makeText(this, "Sign up success", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else
        {
            Toast.makeText(this, "Signup Failed. Password mismatch", Toast.LENGTH_SHORT).show()
        }
    }
}