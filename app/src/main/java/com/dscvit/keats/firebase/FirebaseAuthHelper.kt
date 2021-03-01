package com.dscvit.keats.firebase

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.navigation.findNavController
import com.dscvit.keats.R
import com.dscvit.keats.ui.auth.AuthViewModel
import com.dscvit.keats.ui.auth.SignInFragmentDirections
import com.dscvit.keats.utils.Constants.PREF_NAME
import com.dscvit.keats.utils.Constants.PRIVATE_MODE
import com.dscvit.keats.utils.longToast
import com.dscvit.keats.utils.shortToast
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class FirebaseAuthHelper(
    val context: Context,
    private val activity: Activity,
    private val viewModel: AuthViewModel
) {
    val sharedPref: SharedPreferences =
        activity.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    var verificationId = ""
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val navController by lazy {
        activity.findNavController(R.id.nav_host_fragment)
    }

    private fun verificationCallbacks() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(
                verification: String,
                p1: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verification, p1)
                verificationId = verification

                sharedPref.edit {
                    putString("VER_ID", verificationId)
                    commit()
                }
                navController.navigate(SignInFragmentDirections.actionSignInFragmentToVerifyOtpFragment())
            }

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signIn(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                if (e is FirebaseAuthInvalidCredentialsException) {
                    context.longToast("Invalid Credentials")
                } else if (e is FirebaseTooManyRequestsException) {
                    context.longToast("Too many requests!")
                }
            }
        }
    }

    fun sendOtp(number: String) {
        verificationCallbacks()
        val phoneNumber = "+91$number"
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signIn(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    context.shortToast("OTP Verified")
                    task.result?.user?.getIdToken(true)
                        ?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                viewModel.signIn(context, it.result?.token.toString())
                            } else {
                                context.longToast("Error in getting ID Token")
                            }
                        }
                } else {
                    context.longToast("Wrong OTP")
                }
            }
    }

    fun authenticate(otp: String) {
        val verId = sharedPref.getString("VER_ID", "")
        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(verId!!, otp)
        signIn(credential)
    }
}
