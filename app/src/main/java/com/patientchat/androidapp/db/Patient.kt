package com.patientchat.androidapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Patient(@PrimaryKey val name: String,
                   @ColumnInfo(name = "ssid") val ssid: String?,
                   @ColumnInfo(name = "password") val password: String?) {

    companion object {
        fun create(name: String) : Patient {
            return Patient(name, name+"_ssid", name+"_password")
        }
    }
}