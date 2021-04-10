package com.dscvit.keats.repository

import com.dscvit.keats.model.clubs.JoinClubRequest
import com.dscvit.keats.model.clubs.KickMemberRequest
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

    fun joinClub(joinClubRequest: JoinClubRequest) = makeRequest {
        apiClient.joinClub(joinClubRequest)
    }

    fun getClubDetails(clubId: String) = makeRequest {
        apiClient.getClubDetails(clubId)
    }

    fun kickMember(kickMemberRequest: KickMemberRequest) = makeRequest {
        apiClient.kickMember(kickMemberRequest)
    }
}
