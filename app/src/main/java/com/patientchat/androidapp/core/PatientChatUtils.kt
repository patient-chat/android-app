package com.patientchat.androidapp.core

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService


class PatientChatUtils {
    companion object {
        public fun hideKeyboard(view: View) {
            val inputMethodManager = view.context!!.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}