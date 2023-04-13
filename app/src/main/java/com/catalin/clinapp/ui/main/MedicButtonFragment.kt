package com.catalin.clinapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.catalin.clinapp.R
import com.catalin.clinapp.databinding.FragmentMedicButtonBinding


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
            val intent = Intent(activity, OfficeHoursActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.explode, R.anim.sit)
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = MedicButtonFragment().apply {}
    }
}