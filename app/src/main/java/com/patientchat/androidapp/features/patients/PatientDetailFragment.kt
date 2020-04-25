package com.patientchat.androidapp.features.patients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.patientchat.androidapp.R
import com.patientchat.androidapp.core.db.Patient
import kotlinx.android.synthetic.main.fragment_patient_detail.*

class PatientDetailFragment : Fragment() {

    companion object {
        val ARG_PATIENT = "patient"

        fun createBundle(patient: Patient): Bundle {
            return bundleOf(ARG_PATIENT to patient)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_patient_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val patient: Patient? = arguments?.getParcelable(ARG_PATIENT)
        if (patient == null) {
            //TODO
        } else {
            textview_name.text = patient.name
            textview_ssid.text = patient.ssid
            textview_password.text = patient.password
        }

        view.findViewById<Button>(R.id.button_list).setOnClickListener {
            findNavController().navigate(R.id.action_PatientDetailFragment_to_PatientListFragment)
        }

        view.findViewById<Button>(R.id.button_chat).setOnClickListener {
            if (patient != null) {
                findNavController().navigate(
                    R.id.action_PatientDetailFragment_to_VideoChatActivity,
                    createBundle(patient)
                )
            }
        }
    }
}