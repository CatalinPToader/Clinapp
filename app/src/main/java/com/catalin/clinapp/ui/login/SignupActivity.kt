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
import com.catalin.clinapp.data.User
import com.catalin.clinapp.databinding.ActivitySignupBinding
import com.catalin.clinapp.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    @Suppress("NAME_SHADOWING")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database = Firebase.database("https://eim-clinapp-default-rtdb.europe-west1.firebasedatabase.app").reference

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = binding.signupEmail
        val password = binding.signupPassword
        val signup = binding.signUpButton
        val loading = binding.loading2

        val firstName = binding.firstName
        val lastName = binding.lastName
        val phone = binding.phone

        signup.setOnClickListener {
            val emailString = email.text.toString()
            val passString = password.text.toString()
            val nameString = firstName.text.toString() + " " + lastName.text.toString()
            val phoneString = phone.text.toString()
            if (TextUtils.isEmpty(emailString) || TextUtils.isEmpty(passString)) {
                val snack = Snackbar.make(loading,
                    "Please enter email and password",
                    Snackbar.LENGTH_LONG)
                snack.setTextColor(Color.RED)
                snack.show()

                return@setOnClickListener
            }

            if (TextUtils.isEmpty(firstName.text.toString()) || TextUtils.isEmpty(lastName.text.toString())) {
                val snack = Snackbar.make(loading,
                    "Please enter your name",
                    Snackbar.LENGTH_LONG)
                snack.setTextColor(Color.RED)
                snack.show()

                return@setOnClickListener
            }

            if (TextUtils.isEmpty(phoneString)) {
                val snack = Snackbar.make(loading,
                    "Please enter your phone number",
                    Snackbar.LENGTH_LONG)
                snack.setTextColor(Color.RED)
                snack.show()

                return@setOnClickListener
            }

            val dataUser = User(nameString, emailString, phoneString)

            loading.visibility = View.VISIBLE

            auth.createUserWithEmailAndPassword(emailString, passString)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser

                        database.child("users").child(user!!.uid).setValue(dataUser)

                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        intent.putExtra("com.catalin.clinapp.dataUser", dataUser)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Failed to create account.",
                            Toast.LENGTH_SHORT).show()
                        loading.visibility = View.GONE
                    }
                }
        }
    }
}