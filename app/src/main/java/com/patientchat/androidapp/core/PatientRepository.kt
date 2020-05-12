package com.patientchat.androidapp.core

import androidx.lifecycle.LiveData
import com.patientchat.androidapp.core.db.Patient
import com.patientchat.androidapp.core.db.PatientDoa

class PatientRepository(private val patientDoa: PatientDoa) {
    val allPatients: LiveData<Patient> = patientDoa.getAlphabetizedWords()

    suspend fun insert(patient: Patient) {
        patientDoa.insert(patient)
    }

    suspend fun deleteAll() {
        patientDoa.deleteAll()
    }
}