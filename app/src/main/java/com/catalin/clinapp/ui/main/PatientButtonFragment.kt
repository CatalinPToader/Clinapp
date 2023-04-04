package com.catalin.clinapp.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.catalin.clinapp.R
import com.catalin.clinapp.databinding.FragmentPatientButtonBinding
import com.google.android.material.snackbar.Snackbar


class PatientButtonFragment : Fragment(R.layout.fragment_patient_button) {
    private lateinit var binding: FragmentPatientButtonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPatientButtonBinding.inflate(inflater, container, false)
        val apptBttn = binding.apptButton
        apptBttn.setOnClickListener {
            val snack = Snackbar.make(apptBttn,
                "Clicked Appointment Button",
                Snackbar.LENGTH_LONG)
            snack.setTextColor(Color.RED)
            snack.show()
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = PatientButtonFragment().apply {}
    }
}