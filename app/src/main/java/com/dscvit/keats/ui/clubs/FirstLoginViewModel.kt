package com.dscvit.keats.ui.clubs

import androidx.lifecycle.ViewModel
import com.dscvit.keats.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class FirstLoginViewModel @Inject constructor(private val repo: AppRepository) : ViewModel() {

    var userProfileMultipart: MultipartBody.Part? = null

    fun updateUserProfile(
        username: RequestBody,
        bio: RequestBody,
        email: RequestBody,
        profilePic: MultipartBody.Part?
    ) =
        repo.updateUserProfile(
            username = username,
            bio = bio,
            email = email,
            profilePic = profilePic
        )
}
