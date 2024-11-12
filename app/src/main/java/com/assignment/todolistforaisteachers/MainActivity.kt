package com.assignment.todolistforaisteachers

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.assignment.todolistforaisteachers.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(3000)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        binding.btnlogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                try{
                    loginDataBase(email, password)
                }
                catch( e : Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_LONG).show()
            }


        }

        //Redirects the user to the Sign Up page when sign up button is clicked
        binding.btnsignUp.setOnClickListener{

            try{
                val intent = Intent(this, SignUp::class.java)
                startActivity(intent)
                finish()
            }

            catch( e : Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun loginDataBase(email: String, password: String){
        val validUsernamePassword = databaseHelper.checkEmailPassword(email, password)

        try {
            if (validUsernamePassword) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainMenu::class.java)
                startActivity(intent)
                finish()
            }

            else {
                Toast.makeText(this, "Login Failed. Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }

        catch(e : Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

}