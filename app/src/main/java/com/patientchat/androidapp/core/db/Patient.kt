package com.patientchat.androidapp.core.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patient_table")
data class Patient(@PrimaryKey @ColumnInfo(name = "name") val name: String,
                   @ColumnInfo(name = "ssid") val ssid: String?,
                   @ColumnInfo(name = "password") val password: String?) {

    companion object {
        fun create(name: String) : Patient {
            return Patient(name, name+"_ssid", name+"_password")
        }
    }
}