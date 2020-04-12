package com.patientchat.androidapp.features.patients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.patientchat.androidapp.R
import com.patientchat.androidapp.core.PatientChatUtils
import com.patientchat.androidapp.core.PatientViewModel
import com.patientchat.androidapp.core.db.Patient
import kotlinx.android.synthetic.main.fragment_add_patient.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddPatientFragment : Fragment() {

    private val patientViewModel: PatientViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_patient, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edittext_patient_name.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                PatientChatUtils.hideKeyboard(v)
            }
        }

        view.findViewById<Button>(R.id.button_next).setOnClickListener {
            var patientName = edittext_patient_name.text.toString()
            if (patientName.isNotEmpty()) {
                var patient = Patient.create(patientName)
                patientViewModel.insert(patient)
                findNavController().navigate(R.id.action_AddPatientFragment_to_PatientListFragment)
            } else {
                Snackbar.make(view, "TODO: Add error handling", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
    }
}
