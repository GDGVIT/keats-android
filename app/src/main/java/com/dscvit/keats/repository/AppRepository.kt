package com.dscvit.keats.repository

import com.dscvit.keats.firebase.FirebaseAuthHelper
import com.google.android.material.textfield.TextInputEditText
import javax.inject.Inject

class AppRepository @Inject constructor(private val firebaseAuthHelper: FirebaseAuthHelper) : BaseRepo() {
    fun sendOtp(numberEditText: TextInputEditText) = firebaseAuthHelper.sendOtp(numberEditText)

    fun verifyOtp(otpEditText: TextInputEditText): String? = firebaseAuthHelper.authenticate(otpEditText)
}
