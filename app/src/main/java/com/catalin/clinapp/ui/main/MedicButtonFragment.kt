package com.catalin.clinapp.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.catalin.clinapp.R
import com.catalin.clinapp.databinding.FragmentMedicButtonBinding
import com.google.android.material.snackbar.Snackbar


class MedicButtonFragment : Fragment(R.layout.fragment_medic_button) {
    private lateinit var binding: FragmentMedicButtonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMedicButtonBinding.inflate(inflater, container, false)
        val apptBttn = binding.hoursButton
        apptBttn.setOnClickListener {
            val snack = Snackbar.make(apptBttn,
                "Clicked Set Hours Button",
                Snackbar.LENGTH_LONG)
            snack.setTextColor(Color.BLUE)
            snack.show()
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = MedicButtonFragment().apply {}
    }
}