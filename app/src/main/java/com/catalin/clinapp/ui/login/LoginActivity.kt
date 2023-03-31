package com.catalin.clinapp.ui.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.catalin.clinapp.R
import com.catalin.clinapp.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.loginEmail
        val password = binding.loginPassword
        val login = binding.loginButton
        val signup = binding.signUpButton
        val loading = binding.loading

        login.setOnClickListener {
            if (TextUtils.isEmpty(username.text.toString()) || TextUtils.isEmpty(password.text.toString())) {

                val snack = Snackbar.make(loading,
                    "Please enter email and password",
                    Snackbar.LENGTH_LONG)
                snack.setTextColor(Color.RED)
                snack.show()

                return@setOnClickListener
            }

            loading.visibility = View.VISIBLE

            auth.signInWithEmailAndPassword(username.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser

                        // TODO: Set up firebase to check user type
                        // TODO: Send to appropriate screen
                        if (user != null) {
                            updateUiWithUser(user)
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        loading.visibility = View.GONE
                    }
                }
        }

        signup.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateUiWithUser(user: FirebaseUser) {
        val welcome = getString(R.string.welcome)
        val displayName = user.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }


}