package com.patientchat.androidapp.features.patients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.patientchat.androidapp.R
import com.patientchat.androidapp.core.PatientViewModel
import com.patientchat.androidapp.core.db.Patient
import kotlinx.android.synthetic.main.fragment_add_patient.*
import kotlinx.android.synthetic.main.fragment_patient_list.*

class PatientListFragment : Fragment() {

    private val patientViewModel: PatientViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_patient_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview_patients)
        val adapter = PatientListAdapter(view.context) // TODO: is this the best way to pass in context?
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        patientViewModel.allPatients.observe(viewLifecycleOwner, Observer { patients ->
            patients?.let {
                adapter.setPatients(it) }
        })

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            findNavController().navigate(R.id.action_PatientListFragment_to_AddPatient)
        }
    }


}