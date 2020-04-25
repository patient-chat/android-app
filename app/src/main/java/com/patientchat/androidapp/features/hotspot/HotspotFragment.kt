package com.patientchat.androidapp.features.hotspot

import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.LocalOnlyHotspotCallback
import android.net.wifi.WifiManager.LocalOnlyHotspotReservation
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.patientchat.androidapp.R
import com.patientchat.androidapp.core.db.Patient
import kotlinx.android.synthetic.main.fragment_hotspot.*

class HotspotFragment : Fragment() {

    private var mReservation: WifiManager.LocalOnlyHotspotReservation? = null

    companion object {
        private val ARG_PATIENT = "patient"

        fun createBundle(patient: Patient): Bundle {
            return bundleOf(ARG_PATIENT to patient)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_hotspot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // on first load, attempt to start hotspot & server
        val patient: Patient? = arguments?.getParcelable(ARG_PATIENT)
        if (patient == null) {
            //TODO
            Toast.makeText(activity, "No patient", Toast.LENGTH_LONG).show()
        } else {
            // if successful this will start the server
            textview_hotspot_details.text = patient.name
            startLocalHotspot()
        }

        // hook up button listeners
    }

    private fun onHotspotConnectedUpdateUI(hotspotName: String?, hotspotPassword: String?) {
        textview_hotspot_details.text = hotspotName+"\n"+hotspotPassword
        button_hotspot.text = getString(R.string.hotspot_active)
        button_hotspot.setBackgroundColor(resources.getColor(R.color.colorOn))
    }

    private fun onHotspotDisconnectedUpdateUI(error: String? = null) {
        textview_hotspot_details.text = error ?: ""
        button_hotspot.text = getString(R.string.hotspot_down)
        button_hotspot.setBackgroundColor(resources.getColor(R.color.colorOff))
    }

    private fun onServerConnectedUpdateUI(url: String) {
        webview_preview.loadUrl(url)
        button_hotspot.text = getString(R.string.server_active)
        button_hotspot.setBackgroundColor(resources.getColor(R.color.colorOn))
    }

    private fun onServerDisconnectedUpdateUI() {
        webview_preview.loadUrl("")
        button_hotspot.text = getString(R.string.server_down)
        button_hotspot.setBackgroundColor(resources.getColor(R.color.colorOff))
    }

    private fun startLocalHotspot() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && activity != null) {
            var manager = activity!!.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager //TODO: Is this safe? Is there a better way?
            manager.startLocalOnlyHotspot(object : LocalOnlyHotspotCallback() {
                override fun onStarted(reservation: LocalOnlyHotspotReservation) {
                    super.onStarted(reservation)
                    mReservation = reservation
                    if (mReservation != null) {
                        startServer()
                        val wifiConfig = mReservation?.wifiConfiguration
                        onHotspotConnectedUpdateUI(wifiConfig?.SSID, wifiConfig?.preSharedKey)
                    }
                }

                override fun onStopped() {
                    super.onStopped()
                    takedownServer()
                    onHotspotDisconnectedUpdateUI()
                }

                override fun onFailed(reason: Int) {
                    super.onFailed(reason)
                    takedownServer()
                    onHotspotDisconnectedUpdateUI("Error: $reason") //TODO make human readable
                }
            }, Handler())
        } else {
            // alert user they must update their os
            Toast.makeText(activity, R.string.os_update_needed, Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun takedownLocalHotspot() {
        mReservation?.close()
        onHotspotDisconnectedUpdateUI()
    }

    private fun startServer() {
        // TODO
        Toast.makeText(activity, "...Attempting to start server...", Toast.LENGTH_LONG).show()
        onServerConnectedUpdateUI("https://agilemanifesto.org/")
    }

    private fun takedownServer() {
        // TODO
        Toast.makeText(activity, "...Attempting to takedown server...", Toast.LENGTH_LONG).show()
        onServerDisconnectedUpdateUI()
    }

}
