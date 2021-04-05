package com.dscvit.keats.ui.profile

import androidx.lifecycle.ViewModel
import com.dscvit.keats.model.profile.UpdateUserRequest
import com.dscvit.keats.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(private val repo: AppRepository) : ViewModel() {
    fun getUserProfile() = repo.getUserProfile()

    fun updateUserProfile(updateUserRequest: UpdateUserRequest) =
        repo.updateUserProfile(updateUserRequest)
}
