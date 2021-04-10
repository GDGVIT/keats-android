package com.dscvit.keats.network

import com.dscvit.keats.model.clubs.ClubsListResponse
import com.dscvit.keats.model.clubs.GetClubDetailsResponse
import com.dscvit.keats.model.clubs.JoinClubRequest
import com.dscvit.keats.model.clubs.JoinClubResponse
import com.dscvit.keats.model.clubs.KickMemberRequest
import com.dscvit.keats.model.clubs.KickMemberResponse
import com.dscvit.keats.model.clubs.PublicClubsListResponse
import com.dscvit.keats.model.login.LoginRequest
import com.dscvit.keats.model.login.LoginResponse
import com.dscvit.keats.model.profile.GetUserProfileResponse
import com.dscvit.keats.model.profile.UpdateUserRequest
import com.dscvit.keats.model.profile.UpdateUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    @POST("api/user/")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("api/user/")
    suspend fun getUserProfile(): Response<GetUserProfileResponse>

    @GET("api/user/clubs")
    suspend fun getClubs(): Response<ClubsListResponse>

    @PATCH("api/user/")
    suspend fun updateUser(@Body updateUserRequest: UpdateUserRequest): Response<UpdateUserResponse>

    @GET("api/clubs/list")
    suspend fun getPublicClubsList(): Response<PublicClubsListResponse>

    @POST("api/clubs/join")
    suspend fun joinClub(@Body joinClubRequest: JoinClubRequest): Response<JoinClubResponse>

    @GET("api/clubs")
    suspend fun getClubDetails(@Query("club_id") clubId: String): Response<GetClubDetailsResponse>

    @POST("api/clubs/kickuser")
    suspend fun kickMember(@Body kickMemberRequest: KickMemberRequest): Response<KickMemberResponse>
}
