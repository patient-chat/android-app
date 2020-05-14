package com.patientchat.androidapp.features

import android.app.Application
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes


class PatientAppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCenter.start(
            this, "d5fc59ed-e505-4565-baf3-c0592625cab8",
            Analytics::class.java, Crashes::class.java
        )
    }
}