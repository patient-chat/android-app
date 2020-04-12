package com.patientchat.androidapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.patientchat.androidapp.db.Patient
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_add_patient.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddPatientFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_patient, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_next).setOnClickListener {
            var patientName = edittext_patient_name.text.toString()
            if (patientName.isNotEmpty()) {
                var patient = Patient.create(patientName)
                Snackbar.make(view, patient.name+" created", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            } else {
                Snackbar.make(view, "TODO: Add error handling", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            //findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }
}
