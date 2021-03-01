package com.dscvit.keats.ui.auth

import androidx.lifecycle.ViewModel
import com.dscvit.keats.repository.AppRepository
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: AppRepository) : ViewModel() {
    fun sendOtp(phoneEditText: TextInputEditText) = repo.sendOtp(phoneEditText)

    fun verifyOtp(otpEditText: TextInputEditText): String? = repo.verifyOtp(otpEditText)
}
