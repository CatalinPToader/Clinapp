package com.catalin.clinapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.catalin.clinapp.data.User
import com.catalin.clinapp.databinding.ActivitySplashBinding
import com.catalin.clinapp.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class LoadActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var dataUser : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database =
            Firebase.database("https://eim-clinapp-default-rtdb.europe-west1.firebasedatabase.app").reference

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = auth.currentUser
        if (user == null)
            sendToLogin()

        if (isFinishing)
            return

        Log.d("load", "Got user: $user")

        database.child("users").child(user!!.uid).get().addOnSuccessListener {
            dataUser = it.getValue<User>()!!
            Log.d("firebase", "Retrieved data: $dataUser")
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("com.catalin.clinapp.dataUser", dataUser)
            startActivity(intent)
            finish()
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
            sendToLogin()
        }
    }

    private fun sendToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}