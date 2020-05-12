package com.patientchat.androidapp.features.hotspot

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.patientchat.androidapp.R
import com.patientchat.androidapp.features.hotspot.RequestPermissionsFragment.OnPermissionRequest
import kotlinx.android.synthetic.main.activity_video_chat.*

class VideoChatActivity : AppCompatActivity(), OnPermissionRequest {

    private val PERMISSIONS_REQUEST_FINE_LOCATION = 65
    private val PERMISSIONS_REQUEST_CHANGE_WIFISTATE = 66
    private val PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE = 67
    private val PERMISSIONS_REQUEST_ACCESS_WIFI_STATE = 68
    private val PERMISSIONS_REQUEST_INTERNET = 69

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
            Log.println(Log.ERROR, getString(R.string.video_chat_activity_label), "Permission not granted: ACCESS_FINE_LOCATION")

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
            Log.println(Log.ERROR, getString(R.string.video_chat_activity_label), "Permission not granted: CHANGE_WIFI_STATE")
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CHANGE_WIFI_STATE), PERMISSIONS_REQUEST_CHANGE_WIFISTATE)
        } else {
            changeWiFiStateGranted = true
        }

        // View Network state required for starting a local server
        var accessNetworkStateGranted = false
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_NETWORK_STATE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.println(Log.ERROR, getString(R.string.video_chat_activity_label), "Permission not granted: ACCESS_NETWORK_STATE")
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_NETWORK_STATE), PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE)
        } else {
            accessNetworkStateGranted = true
        }

        // View Wifi state required for starting a local server
        var accessWiFiStateGranted = false
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_WIFI_STATE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.println(Log.ERROR, getString(R.string.video_chat_activity_label), "Permission not granted: ACCESS_WIFI_STATE")
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_WIFI_STATE), PERMISSIONS_REQUEST_ACCESS_WIFI_STATE)
        } else {
            accessWiFiStateGranted = true
        }

        // Internet required for starting a local server
        var internetGranted = false
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.println(Log.ERROR, getString(R.string.video_chat_activity_label), "Permission not granted: INTERNET")
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.INTERNET), PERMISSIONS_REQUEST_INTERNET)
        } else {
            internetGranted = true
        }

        // if all permissions are granted, show Hotspot Fragment
        if (fineLocationGranted && changeWiFiStateGranted && accessWiFiStateGranted && internetGranted) {
            showHotspotFragment()
        }
    }

    private fun showHotspotFragment() {
        findNavController(R.id.nav_host_fragment).navigate(
            R.id.action_RequestPermissionsFragment_to_HotspotFragment, intent.extras
        )
    }

    // This is used by the RequestPermissions fragment to trigger another request to the user
    override fun onPermissionRequest() {
        checkPermissions()
    }
}
