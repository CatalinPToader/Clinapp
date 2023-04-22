package com.catalin.clinapp.ui.main

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.catalin.clinapp.data.User
import com.catalin.clinapp.databinding.ActivitySplashBinding
import com.catalin.clinapp.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlin.properties.Delegates

class LoadActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var dataUser: User
    private var conn by Delegates.notNull<Boolean>()
    private val scope =
        CoroutineScope(Job() + Dispatchers.Main)
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database =
            Firebase.database("https://eim-clinapp-default-rtdb.europe-west1.firebasedatabase.app")

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = auth.currentUser
        if (user == null)
            sendToLogin()

        if (isFinishing)
            return

        val toast = Toast.makeText(
            baseContext, "Network error, sending to login!",
            Toast.LENGTH_SHORT
        )

        Log.d("load", "Got user: $user")

        database.reference.child("users").child(user!!.uid).get().addOnSuccessListener {
            if (job?.isCompleted == true)
                return@addOnSuccessListener
            job?.cancel()
            dataUser = it.getValue<User>()!!
            Log.d("firebase", "Retrieved data: $dataUser")
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("com.catalin.clinapp.dataUser", dataUser)
            startActivity(intent)
            finish()
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
            sendToLogin()
        }

        val connectedRef = database.getReference(".info/connected")

        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                conn = snapshot.getValue<Boolean>() ?: false
                if (conn) {
                    Log.d(TAG, "connected")
                } else {
                    Log.d(TAG, "not connected")
                    job = scope.launch {
                        delay(3000L)
                        if (!isFinishing) {
                            toast.show()
                            sendToLogin()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Listener was cancelled")
            }
        })
    }

    private fun sendToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}