package com.patientchat.androidapp.features.hotspot

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.patientchat.androidapp.R
import com.patientchat.androidapp.features.MainActivity
import com.patientchat.androidapp.features.hotspot.RequestPermissionsFragment.OnPermissionRequest
import com.patientchat.androidapp.features.patients.PatientDetailFragment
import kotlinx.android.synthetic.main.activity_video_chat.*

class VideoChatActivity : AppCompatActivity(), OnPermissionRequest {

    private val PERMISSIONS_REQUEST_FINE_LOCATION = 65
    private val PERMISSIONS_REQUEST_CHANGE_WIFISTATE = 66

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_chat)
        setSupportActionBar(toolbar)

        // this checks for permissions & if able, starts the hotspot fragment with the patient in the intent
        checkPermissions()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            // currently only fine location has a popup dialog
            // TODO: advise user if they need to go to settings and update CHANGE WIFI STATE
            if (requestCode == PERMISSIONS_REQUEST_FINE_LOCATION
                && ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
                showHotspotFragment()
            }
        }
    }

    private fun checkPermissions() {
        // Fine location required for starting a local hotspot
        var fineLocationGranted = false
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_FINE_LOCATION)
        } else {
            fineLocationGranted = true
        }

        // Change Wifi state required for starting a local hotspot
        var changeWiFiStateGranted = false
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CHANGE_WIFI_STATE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CHANGE_WIFI_STATE), PERMISSIONS_REQUEST_CHANGE_WIFISTATE)
        } else {
            changeWiFiStateGranted = true
        }

        // if all permissions are granted, show Hotspot Fragment
        if (fineLocationGranted && changeWiFiStateGranted) {
            showHotspotFragment()
        }
    }

    private fun showHotspotFragment() {
        findNavController(R.id.nav_host_fragment)?.navigate(
            R.id.action_RequestPermissionsFragment_to_HotspotFragment, intent.extras)
//        findNavController(R.id.nav_host_fragment)
//            .setGraph(R.navigation.nav_video_chat, intent.extras)
    }

    // This is used by the RequestPermissions fragment to trigger another request to the user
    override fun onPermissionRequest() {
        checkPermissions()
    }
}
