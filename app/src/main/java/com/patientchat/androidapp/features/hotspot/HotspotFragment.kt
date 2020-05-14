package com.patientchat.androidapp.features.hotspot

import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.LocalOnlyHotspotCallback
import android.net.wifi.WifiManager.LocalOnlyHotspotReservation
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.patientchat.androidapp.R
import com.patientchat.androidapp.core.db.Patient
import com.patientchat.androidapp.core.server.LocalWebServer
import kotlinx.android.synthetic.main.fragment_hotspot.*

class HotspotFragment : Fragment() {

    private var mReservation: WifiManager.LocalOnlyHotspotReservation? = null
    private var mServer: LocalWebServer? = null
    private val DEFAULT_PORT = 8080
    private var mHotspotUp = false
    private var mServerUp = false

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
        button_hotspot.setOnClickListener {
            if (mHotspotUp) {
                takedownLocalHotspot()
            } else {
                startLocalHotspot()
            }
        }

        button_server.setOnClickListener {
            if (mServerUp) {
                takedownServer()
            } else {
                startServer()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        takedownServer()
        takedownLocalHotspot()
    }

    private fun onHotspotConnectedUpdateUI(hotspotName: String?, hotspotPassword: String?) {
        mHotspotUp = true
        textview_hotspot_details.text = getString(R.string.hotspot_details, hotspotName, hotspotPassword)
        button_hotspot.text = getString(R.string.hotspot_active)
        button_hotspot.setBackgroundColor(resources.getColor(R.color.colorOn))
    }

    private fun onHotspotDisconnectedUpdateUI(error: String? = null) {
        mHotspotUp = false
        if (textview_hotspot_details != null) {
            textview_hotspot_details.text = error ?: ""
            button_hotspot.text = getString(R.string.hotspot_down)
            button_hotspot.setBackgroundColor(resources.getColor(R.color.colorOff))
        }
    }

    private fun onServerConnectedUpdateUI(url: String, ipAddress: String, port: Int) {
        mServerUp = true
        webview_preview.loadUrl(url)
        textview_server_details.text = getString(R.string.server_details, ipAddress, port)
        button_server.text = getString(R.string.server_active)
        button_server.setBackgroundColor(resources.getColor(R.color.colorOn))
    }

    private fun onServerDisconnectedUpdateUI() {
        mServerUp = false
        if (webview_preview != null) {
            webview_preview.loadUrl("")
            button_server.text = getString(R.string.server_down)
            button_server.setBackgroundColor(resources.getColor(R.color.colorOff))
        }
    }

    private fun startLocalHotspot() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && activity != null) {
            var manager = activity!!.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager //TODO: Is this safe? Is there a better way?

            // TODO set hotspot ssid & preSharedKey programattically without reflection (http://www.programmersought.com/article/4227986029/)
            manager.startLocalOnlyHotspot(object : LocalOnlyHotspotCallback() {
                override fun onStarted(reservation: LocalOnlyHotspotReservation) {
                    super.onStarted(reservation)
                    mReservation = reservation
                    if (mReservation != null) {
                        val wifiConfig = mReservation?.wifiConfiguration
                        onHotspotConnectedUpdateUI(wifiConfig?.SSID, wifiConfig?.preSharedKey)
                        startServer()
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

    private fun takedownLocalHotspot() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && activity != null) {
            mReservation?.close()
            onHotspotDisconnectedUpdateUI()
        }
    }

    private fun startServer() {
        try {
            if (mServer == null) {
                mServer = LocalWebServer(DEFAULT_PORT)
            }
            mServer?.start()

            val wm =
                activity?.applicationContext?.getSystemService(WIFI_SERVICE) as WifiManager?
            val ip: String = Formatter.formatIpAddress(wm!!.connectionInfo.ipAddress)
            onServerConnectedUpdateUI("https://agilemanifesto.org/", ip, DEFAULT_PORT)
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }

    private fun takedownServer() {
        mServer?.stop()
        onServerDisconnectedUpdateUI()
    }

}
