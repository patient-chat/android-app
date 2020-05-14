package com.patientchat.androidapp.features.patients

import com.patientchat.androidapp.core.db.Patient

interface PatientItemClickListener {
    fun onPatientClick(patient: Patient)
}