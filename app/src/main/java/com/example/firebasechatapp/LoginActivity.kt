package com.example.firebasechatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebasechatapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    @Inject lateinit var dbref: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.loginBtn2.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }


        binding.loginBtn.setOnClickListener {
            val email = binding.mailTxt.text.toString()
            val password = binding.mailTxt2.text.toString()

            if (isValidEmail(email)) {
                emailAuth(email, password)
            } else {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun emailAuth(email: String, password: String) {
        if (password.isNotEmpty()) {
            dbref.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {

                        val errorMessage = task.exception?.message ?: "Login failed"
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
        } else {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_LONG).show()
        }
    }


    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
