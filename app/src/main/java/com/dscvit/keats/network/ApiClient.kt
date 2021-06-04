package com.dscvit.keats.network

import com.dscvit.keats.model.clubs.JoinClubRequest
import com.dscvit.keats.model.clubs.KickMemberRequest
import com.dscvit.keats.model.login.LoginRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    suspend fun updateUserProfile(
        username: RequestBody,
        bio: RequestBody,
        email: RequestBody,
        profilePic: MultipartBody.Part?
    ) = getResult {
        api.updateUser(username = username, bio = bio, email = email, profilePic = profilePic)
    }

    suspend fun createClub(
        clubName: RequestBody,
        clubStatus: RequestBody,
        pageSync: RequestBody,
        clubPic: MultipartBody.Part?,
        clubBook: MultipartBody.Part?
    ) = getResult {
        api.createClub(
            clubName = clubName,
            clubStatus = clubStatus,
            pageSync = pageSync,
            clubPic = clubPic,
            clubBook = clubBook
        )
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

    suspend fun uploadFile(file: MultipartBody.Part) = getResult {
        api.uploadFile(file)
    }
}
