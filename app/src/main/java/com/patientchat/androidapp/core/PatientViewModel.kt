package com.patientchat.androidapp.core

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.patientchat.androidapp.core.db.Patient
import com.patientchat.androidapp.core.db.PatientChatDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PatientViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PatientRepository
    val allPatients: LiveData<Patient>

    init {
        val patientDoa = PatientChatDatabase.getDatabase(application).patientDao()
        repository = PatientRepository(patientDoa)
        allPatients = repository.allPatients
    }

    fun insert(patient: Patient) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(patient)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }
}