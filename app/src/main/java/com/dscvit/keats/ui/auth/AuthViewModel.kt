package com.dscvit.keats.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import com.dscvit.keats.repository.AppRepository
import com.dscvit.keats.utils.shortToast
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: AppRepository) : ViewModel() {
    fun signIn(context: Context, token: String) {
        context.shortToast(token)
    }
}
