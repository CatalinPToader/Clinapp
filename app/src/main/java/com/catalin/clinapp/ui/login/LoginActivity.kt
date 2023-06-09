package com.catalin.clinapp.ui.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.catalin.clinapp.R
import com.catalin.clinapp.data.User
import com.catalin.clinapp.databinding.ActivityLoginBinding
import com.catalin.clinapp.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database =
            Firebase.database("https://eim-clinapp-default-rtdb.europe-west1.firebasedatabase.app").reference

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.loginEmail
        val password = binding.loginPassword
        val login = binding.loginButton
        val signup = binding.signUpButton
        val loading = binding.loading

        login.setOnClickListener {
            val emailString = username.text.toString()
            val passString = password.text.toString()
            if (TextUtils.isEmpty(emailString) || TextUtils.isEmpty(passString)) {

                val snack = Snackbar.make(
                    loading,
                    "Please enter email and password",
                    Snackbar.LENGTH_LONG
                )
                snack.setTextColor(Color.RED)
                snack.show()

                return@setOnClickListener
            }

            loading.visibility = View.VISIBLE

            auth.signInWithEmailAndPassword(emailString, passString)
                .addOnCompleteListener(this) { task ->
                    loading.visibility = View.GONE
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("login", "signed in")
                        val user = auth.currentUser!!
                        loginUser(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("login", "sign in failure", task.exception)
                        try {
                            throw task.exception!!
                        } catch(e: FirebaseNetworkException) {
                            Toast.makeText(
                                baseContext, "Authentication failed due to network errors.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: java.lang.Exception) {
                            if (auth.currentUser != null) {
                                loginUser(auth.currentUser!!)
                            } else {
                                Toast.makeText(
                                    baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
        }

        signup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    private fun loginUser(user: FirebaseUser) {
        database.child("users").child(user.uid).get().addOnSuccessListener {
            val dataUser = it.getValue<User>()
            if (dataUser != null) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("com.catalin.clinapp.dataUser", dataUser)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down)
                finish()
            }
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }
}