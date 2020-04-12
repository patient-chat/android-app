package com.patientchat.androidapp.core.db

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patient_table")
data class Patient(@PrimaryKey @ColumnInfo(name = "name") val name: String,
                   @ColumnInfo(name = "ssid") val ssid: String?,
                   @ColumnInfo(name = "password") val password: String?) : Parcelable {

    companion object CREATOR : Parcelable.Creator<Patient> {
        fun create(name: String) : Patient {
            return Patient(name, name+"_ssid", name+"_password")
        }

        override fun createFromParcel(parcel: Parcel): Patient {
            return Patient(parcel)
        }

        override fun newArray(size: Int): Array<Patient?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(ssid)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0 //TODO
    }
}