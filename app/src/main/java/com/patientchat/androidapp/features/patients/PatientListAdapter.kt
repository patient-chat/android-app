package com.patientchat.androidapp.features.patients

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.patientchat.androidapp.R
import com.patientchat.androidapp.core.db.Patient

class PatientListAdapter internal constructor(context: Context)
    : RecyclerView.Adapter<PatientListAdapter.PatientViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var patients = listOf<Patient>(Patient.create("Smith"))
//    private var patients = emptyList<Patient>()

    inner class PatientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patientItemView: TextView = itemView.findViewById(R.id.textView_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_patient_item, parent, false)
        return PatientViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        patients.size
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val current = patients[position]
        holder.patientItemView.text = current.name
    }

    internal fun setPatients(patients: List<Patient>) {
        this.patients = patients
        notifyDataSetChanged()
    }
}