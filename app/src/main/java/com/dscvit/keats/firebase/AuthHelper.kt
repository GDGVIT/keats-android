package com.dscvit.keats.firebase

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import com.dscvit.keats.utils.Constants.PREF_NAME
import com.dscvit.keats.utils.Constants.PRIVATE_MODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider

class AuthHelper(val context: Context, private val view: View, private val activity: Activity) {
    val sharedPref: SharedPreferences = activity.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    var verificationId = ""
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
}
