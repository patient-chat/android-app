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

    private val PERMISSIONS_REQUEST = 65
    private val mPermissionsNeeded = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.INTERNET
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // The first fragment shown is the RequestPermissionsFragment, which calls checkPermissions.
        // If permissions are granted, the hotspot fragment is started with the patient in the intent.
        setContentView(R.layout.activity_video_chat)
        setSupportActionBar(toolbar)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var successes = 0;
        var iterator = 0;
        permissions.forEach {
            if (grantResults[iterator] == PackageManager.PERMISSION_GRANTED) {
                successes++
            }
            iterator++
        }
        if (successes == permissions.size) {
            showHotspotFragment()
        }
    }

    private fun checkPermissions() {
        var permissionsNotGranted = mutableListOf<String>()

        mPermissionsNeeded.forEach { permission ->
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                Log.println(
                    Log.ERROR,
                    getString(R.string.video_chat_activity_label),
                    "Permission not granted: "+permission
                )
                permissionsNotGranted.add(permission)
            }
        }

        if (permissionsNotGranted.size > 0) {
            ActivityCompat.requestPermissions(this,
                permissionsNotGranted.toTypedArray(), PERMISSIONS_REQUEST)
        } else {
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
