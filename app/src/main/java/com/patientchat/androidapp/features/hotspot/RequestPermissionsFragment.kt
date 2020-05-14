package com.patientchat.androidapp.features.hotspot

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.patientchat.androidapp.R

// NOTE: Activities that use this fragment must implement OnPermissionRequest
class RequestPermissionsFragment : Fragment() {
    private lateinit var mListener: OnPermissionRequest

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_permissions, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as OnPermissionRequest;
        } catch (castException: ClassCastException) {
            Log.println(Log.ERROR, getString(R.string.request_permissions_fragment_label),"Does your activity implement OnPermissionRequest? It needs to...")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListener.onPermissionRequest()

        view.findViewById<Button>(R.id.button_request_permissions).setOnClickListener {
            mListener.onPermissionRequest()
        }
    }

    interface OnPermissionRequest {
        fun onPermissionRequest()
    }
}