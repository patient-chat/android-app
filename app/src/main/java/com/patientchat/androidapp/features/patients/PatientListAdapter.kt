package com.patientchat.androidapp.features.patients

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.patientchat.androidapp.R
import com.patientchat.androidapp.core.db.Patient

class PatientListAdapter internal constructor(
    context: Context,
    private val patientItemClickListener: PatientItemClickListener
) : RecyclerView.Adapter<PatientListAdapter.PatientViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var patients = emptyList<Patient>() // Cached copy of patients

    inner class PatientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patientItemView: TextView = itemView.findViewById(R.id.textView_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_patient_item, parent, false)
        return PatientViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val current = patients[position]
        holder.patientItemView.text = current.name
        holder.patientItemView.setOnClickListener {
            patientItemClickListener.onPatientClick(current)
        }
    }

    internal fun setPatients(words: List<Patient>) {
        this.patients = words
        notifyDataSetChanged()
    }

    override fun getItemCount() = patients.size
}