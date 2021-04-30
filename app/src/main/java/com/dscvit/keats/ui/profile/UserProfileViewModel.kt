package com.dscvit.keats.ui.profile

import androidx.lifecycle.ViewModel
import com.dscvit.keats.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(private val repo: AppRepository) : ViewModel() {
    fun getUserProfile() = repo.getUserProfile()

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

    fun uploadFile(file: MultipartBody.Part) = repo.uploadFile(file)
}
