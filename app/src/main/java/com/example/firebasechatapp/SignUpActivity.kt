package com.example.firebasechatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    @Inject lateinit var dbref: FirebaseAuth
    @Inject lateinit var mbref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.loginBtn.setOnClickListener {
            val name = binding.mailTxt3.text.toString()
            val email = binding.mailTxt.text.toString()
            val password = binding.mailTxt2.text.toString()
            signUp(name, email, password)
        }
    }

    private fun signUp(name: String, email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            dbref.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userId = dbref.currentUser!!.uid
                        addUserToDataBase(name, email, userId)

                        val intent = Intent(this, MainActivity::class.java)
                        finish()
                        startActivity(intent)
                    } else {
                        val errorMessage = task.exception?.message ?: "Error occurred"
                        Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
        } else {
            Toast.makeText(applicationContext, "Please enter both email and password", Toast.LENGTH_LONG).show()
        }
    }

    private fun addUserToDataBase(name: String, email: String, uid: String) {
        val user = User(name, email, uid)
        mbref.child(uid).setValue(user)
            .addOnCompleteListener {
                Log.d("userdata", "User data saved successfully")
            }
            .addOnFailureListener {
                Log.d("userdata", "Failed to save user data: ${it.message}")
            }
    }
}
