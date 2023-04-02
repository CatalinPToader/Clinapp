package com.catalin.clinapp.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.catalin.clinapp.data.User
import com.catalin.clinapp.databinding.ActivityMainPatientBinding
import com.catalin.clinapp.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PatientMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainPatientBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var dataUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database =
            Firebase.database("https://eim-clinapp-default-rtdb.europe-west1.firebasedatabase.app").reference

        //val user = auth.currentUser

        dataUser = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelable("com.catalin.clinapp.dataUser", User::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.extras?.getParcelable<User>("com.catalin.clinapp.dataUser")!!
        }


        binding = ActivityMainPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = binding.userName
        userName.text = dataUser.name

        val signout = binding.patientSignOutBttn
        signout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

}