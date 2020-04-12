package com.patientchat.androidapp

import com.patientchat.androidapp.db.Patient
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PatientUnitTest {
    @Test
    fun patient_isCreated() {
        var patient = Patient.create("Bob")

        assertEquals("Bob", patient.name)
        assertEquals("Bob_ssid", patient.ssid)
        assertEquals("Bob_password", patient.password)
    }
}
