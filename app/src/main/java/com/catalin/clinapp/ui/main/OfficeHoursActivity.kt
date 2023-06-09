package com.catalin.clinapp.ui.main

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.catalin.clinapp.R
import com.catalin.clinapp.data.DataSchedule
import com.catalin.clinapp.data.Schedule
import com.catalin.clinapp.databinding.ActivityOfficeHoursBinding
import com.catalin.clinapp.functional.DayAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class OfficeHoursActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOfficeHoursBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var savedSchedule: Schedule? = null
    private var schedule = Schedule()
    private var loaded = false
    private val scope =
        CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
                overridePendingTransition(R.anim.sit, R.anim.implode)
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        auth = Firebase.auth

        database =
            Firebase.database("https://eim-clinapp-default-rtdb.europe-west1.firebasedatabase.app").reference

        binding = ActivityOfficeHoursBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = auth.currentUser!!
        val saveButton = binding.saveBttn
        val loading = binding.loadingHours

        scope.launch {
            var repeat = 1
            while (true) {
                delay(5000L * repeat)
                if (!loaded) {
                    val toast = Toast.makeText(
                        baseContext,
                        "Network is slow or unreachable.\nYou can wait or try again later.",
                        Toast.LENGTH_SHORT
                    )
                    toast.duration = Toast.LENGTH_LONG
                    toast.show()
                    repeat++
                } else {
                    break
                }
            }
        }

        database.child("schedules").child(user.uid).get().addOnSuccessListener {
            if (it.getValue<DataSchedule>() != null) {
                schedule = Schedule(it.getValue<DataSchedule>()!!)
                savedSchedule = Schedule(it.getValue<DataSchedule>()!!)
                schedule.setCompareSchedule(savedSchedule!!)

                schedule.scheduleCompareListener.add {
                    if (!schedule.same)
                        saveButton.visibility = View.VISIBLE
                    else
                        saveButton.visibility = View.GONE
                }

                val recyclerView = binding.recyclerView
                recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

                recyclerView.adapter = DayAdapter(schedule)
                recyclerView.visibility = View.VISIBLE
                loading.visibility = View.GONE
                loaded = true
            } else {
                dataFail(saveButton, false)
            }
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
            dataFail(saveButton, false)
        }

        saveButton.setOnClickListener {
            database.child("schedules").child(user.uid).setValue(schedule.createDataSchedule())
            database.child("schedules").child(user.uid).get().addOnSuccessListener {
                savedSchedule = Schedule(it.getValue<DataSchedule>()!!)
                schedule.setCompareSchedule(savedSchedule!!)
            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
                dataFail(saveButton, true)
            }
            saveButton.visibility = View.GONE
        }

    }


    private fun dataFail(saveButton: Button, showSnack: Boolean) {
        if (showSnack) {
            val snack = Snackbar.make(
                saveButton,
                "Could not save data, retry",
                Snackbar.LENGTH_LONG
            )
            snack.setTextColor(Color.RED)
            snack.show()
        }
        saveButton.visibility = View.VISIBLE
    }
}