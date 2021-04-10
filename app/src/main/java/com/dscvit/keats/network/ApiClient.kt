package com.dscvit.keats.network

import com.dscvit.keats.model.clubs.JoinClubRequest
import com.dscvit.keats.model.clubs.KickMemberRequest
import com.dscvit.keats.model.login.LoginRequest
import com.dscvit.keats.model.profile.UpdateUserRequest
import javax.inject.Inject

class ApiClient @Inject constructor(
    private val api: ApiInterface
) : BaseApiClient() {
    suspend fun loginUser(loginRequest: LoginRequest) = getResult {
        api.loginUser(loginRequest)
    }

    suspend fun getClubs() = getResult {
        api.getClubs()
    }

    suspend fun getUserProfile() = getResult {
        api.getUserProfile()
    }

    suspend fun updateUserProfile(updateUserRequest: UpdateUserRequest) = getResult {
        api.updateUser(updateUserRequest)
    }

    suspend fun getPublicClubsList() = getResult {
        api.getPublicClubsList()
    }

    suspend fun joinClub(joinClubRequest: JoinClubRequest) = getResult {
        api.joinClub(joinClubRequest)
    }

    suspend fun getClubDetails(clubId: String) = getResult {
        api.getClubDetails(clubId)
    }

    suspend fun kickMember(kickMemberRequest: KickMemberRequest) = getResult {
        api.kickMember(kickMemberRequest)
    }
}
