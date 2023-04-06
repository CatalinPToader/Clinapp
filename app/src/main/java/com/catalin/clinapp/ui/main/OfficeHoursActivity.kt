package com.catalin.clinapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.catalin.clinapp.databinding.ActivityOfficeHoursBinding
import com.catalin.clinapp.functional.DayAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class OfficeHoursActivity: AppCompatActivity() {
    private lateinit var binding: ActivityOfficeHoursBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database =
            Firebase.database("https://eim-clinapp-default-rtdb.europe-west1.firebasedatabase.app").reference

        binding = ActivityOfficeHoursBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        recyclerView.adapter = DayAdapter()
    }

}