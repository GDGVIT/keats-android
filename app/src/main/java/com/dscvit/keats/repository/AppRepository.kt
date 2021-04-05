package com.dscvit.keats.repository

import com.dscvit.keats.model.login.LoginRequest
import com.dscvit.keats.model.profile.UpdateUserRequest
import com.dscvit.keats.network.ApiClient
import javax.inject.Inject

class AppRepository @Inject constructor(private val apiClient: ApiClient) : BaseRepo() {
    fun loginUser(loginRequest: LoginRequest) = makeRequest {
        apiClient.loginUser(loginRequest)
    }

    fun getClubs() = makeRequest {
        apiClient.getClubs()
    }

    fun getUserProfile() = makeRequest {
        apiClient.getUserProfile()
    }

    fun updateUserProfile(updateUserRequest: UpdateUserRequest) = makeRequest {
        apiClient.updateUserProfile(updateUserRequest)
    }

    fun getPublicClubsList() = makeRequest {
        apiClient.getPublicClubsList()
    }
}
