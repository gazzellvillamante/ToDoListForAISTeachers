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

            try{
                signupDatabase(username,password,confirmpassword)
            }
            catch(e : Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }

        }

    }


    private fun signupDatabase(username: String, password: String, confirmpassword: String){
        val userExists = databaseHelper.checkUserExists(username)

        try {

            if (username.isEmpty() || password.isEmpty() || confirmpassword.isEmpty()) {
                Toast.makeText(this,"Sign up failed. Enter required fields data",Toast.LENGTH_SHORT
                ).show()

                return
            }

            else if (userExists) {
                Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT)
                    .show()

                return
            }

            else if (password.length < 8 || confirmpassword.length < 8) {
                Toast.makeText(this, "Password should be atleast 8 characters", Toast.LENGTH_SHORT)
                    .show()

                return
            }

            else if (password != confirmpassword) {
                Toast.makeText(this, "Signup Failed. Password mismatch", Toast.LENGTH_SHORT).show()

                return
            }

            else {
                val insertRowId = databaseHelper.addUser(username, password,confirmpassword)
                val intent = Intent(this, MainActivity::class.java)

                // Checks whether inserting of data into the db was successful or not
                if (insertRowId != -1L) {
                    Toast.makeText(this, "User successfully registered", Toast.LENGTH_SHORT).show()

                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(this, "Error registering user", Toast.LENGTH_SHORT).show()
                }

            }
        }

        catch( e : Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }
}