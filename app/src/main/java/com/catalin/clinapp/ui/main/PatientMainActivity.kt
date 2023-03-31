package com.catalin.clinapp.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.catalin.clinapp.databinding.ActivityMainPatientBinding
import com.catalin.clinapp.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PatientMainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainPatientBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        binding = ActivityMainPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = auth.currentUser
        val userName = binding.userName

        if (user != null) {
            userName.text = user.displayName
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}