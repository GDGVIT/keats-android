package com.dscvit.keats.repository

import com.dscvit.keats.model.clubs.JoinClubRequest
import com.dscvit.keats.model.clubs.KickMemberRequest
import com.dscvit.keats.model.clubs.LeaveClubRequest
import com.dscvit.keats.model.login.LoginRequest
import com.dscvit.keats.network.ApiClient
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    fun updateUserProfile(
        username: RequestBody,
        bio: RequestBody,
        email: RequestBody,
        profilePic: MultipartBody.Part?
    ) = makeRequest {
        apiClient.updateUserProfile(
            username = username,
            bio = bio,
            email = email,
            profilePic = profilePic
        )
    }

    fun createClub(
        clubName: RequestBody,
        clubStatus: RequestBody,
        pageSync: RequestBody,
        clubPic: MultipartBody.Part?,
        clubBook: MultipartBody.Part?
    ) = makeRequest {
        apiClient.createClub(
            clubName = clubName,
            clubStatus = clubStatus,
            pageSync = pageSync,
            clubPic = clubPic,
            clubBook = clubBook
        )
    }

    fun editClub(
        clubId: RequestBody,
        clubName: RequestBody,
        pageSync: RequestBody,
        clubPic: MultipartBody.Part?,
        clubBook: MultipartBody.Part?
    ) = makeRequest {
        apiClient.editClub(
            clubId = clubId,
            clubName = clubName,
            pageSync = pageSync,
            clubPic = clubPic,
            clubBook = clubBook
        )
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

    fun uploadFile(file: MultipartBody.Part) = makeRequest {
        apiClient.uploadFile(file)
    }

    fun leaveClub(leaveClubRequest: LeaveClubRequest) = makeRequest {
        apiClient.leaveClub(leaveClubRequest)
    }
}
