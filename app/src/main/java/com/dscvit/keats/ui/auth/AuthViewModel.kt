package com.dscvit.keats.ui.auth

import androidx.lifecycle.ViewModel
import com.dscvit.keats.model.login.LoginRequest
import com.dscvit.keats.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: AppRepository) : ViewModel() {
    fun signIn(loginRequest: LoginRequest) = repo.loginUser(loginRequest)
}
