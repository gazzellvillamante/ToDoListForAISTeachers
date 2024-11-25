package com.assignment.todolistforaisteachers

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.assignment.todolistforaisteachers.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.database



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var databaseHelper: DatabaseHelper

    private lateinit var authFirebase: FirebaseAuth
    private lateinit var databaseFirebase: DatabaseReference




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(3000)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)
        val context = this

        //initialize firebase
        authFirebase = Firebase.auth
        //initialize firebase database
        databaseFirebase = Firebase.database.reference

        binding.btnlogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()


            //Check if device is online
            //If online use Firebase database
            //If not use Local SQLite database
            if(isDeviceOnline(context)){

                if(email.isNotEmpty() && password.isNotEmpty()){
                    try{
                        loginFirebase(email, password)
                    } catch(e:Exception){
                        Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Please enter email and password", Toast.LENGTH_LONG).show()
                }

            } else {
                if(email.isNotEmpty() && password.isNotEmpty()){
                    try{
                        loginDataBase(email, password)
                        databaseHelper.setLoginUser(email, password)
                    }
                    catch( e : Exception){
                        Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    Toast.makeText(this, "Please enter email and password", Toast.LENGTH_LONG).show()
                }
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

        binding.btnLoginGoogle.setOnClickListener {
            try{
                loginGoogle()
            }
            catch(e:Exception) {
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

    //Login using google
    private fun loginGoogle(){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://accounts.google.com/Login"))
        startActivity(intent)
    }

    //Check if device is online
    fun isDeviceOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)

    }

    //Sign in using firebase if device is online
    private fun loginFirebase(email:String, password:String){
        try{
            authFirebase.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val user = authFirebase.currentUser
                    updateUI(user)
                }
            }
        } catch(e:Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun updateUI(user:FirebaseUser?){
        startActivity(Intent(this, MainMenu::class.java))
        finish()
    }



}