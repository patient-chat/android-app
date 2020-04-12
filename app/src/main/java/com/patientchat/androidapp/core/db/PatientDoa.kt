package com.patientchat.androidapp.core.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PatientDoa {

    @Query("SELECT * from patient_table ORDER BY name ASC")
    fun getAlphabetizedWords(): LiveData<List<Patient>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Patient)

    @Query("DELETE FROM patient_table")
    suspend fun deleteAll()
}