package com.assignment.todolistforaisteachers

import android.os.Bundle
import android.widget.Toast
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.assignment.todolistforaisteachers.databinding.ActivitySignUpBinding
import com.assignment.todolistforaisteachers.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignUp : AppCompatActivity() {

    private lateinit var databaseHelper : DatabaseHelper

    private lateinit var binding: ActivitySignUpBinding

    private lateinit var authFirebase: FirebaseAuth
    private lateinit var databaseFirebase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        //initialize firebase
        authFirebase = Firebase.auth
        //initialize firebase database
        databaseFirebase = Firebase.database.reference

        binding.btnSignUp.setOnClickListener{
            val username = binding.txtUsername.text.toString()
            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()
            val confirmPassword = binding.txtConfirmPassword.text.toString()

            try{
                signupDatabase(username,email, password,confirmPassword)
            }
            catch(e : Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }

        }

    }


    private fun signupDatabase(username: String, email: String, password: String, confirmPassword: String){
        val userExists = databaseHelper.checkUserExists(username, email)

        try {

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this,"Sign up failed. Enter required fields data",Toast.LENGTH_SHORT
                ).show()

                return
            }

            else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()

                return
            }

            else if (userExists) {
                Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT)
                    .show()

                return
            }

            else if (password.length < 8 || confirmPassword.length < 8) {
                Toast.makeText(this, "Password should be at least 8 characters", Toast.LENGTH_SHORT)
                    .show()

                return
            }

            else if (password != confirmPassword) {
                Toast.makeText(this, "Signup Failed. Password mismatch", Toast.LENGTH_SHORT).show()

                return
            }

            else {
                val insertRowId = databaseHelper.addUser(username, email, password,confirmPassword)
                val intent = Intent(this, MainActivity::class.java)

                authFirebase.createUserWithEmailAndPassword(email ,password)
                saveData()

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


    //Save data to Firebase realtime database
    private fun saveData() {
        val username = binding.txtUsername.text.toString()
        val email = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        val confirmPassword = binding.txtConfirmPassword.text.toString()

        val user = UserModel(username, email, password, confirmPassword)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        databaseFirebase.child("user").child(userId).setValue(user)
    }

}