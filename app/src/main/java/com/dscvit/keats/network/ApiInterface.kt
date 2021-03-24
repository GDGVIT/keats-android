package com.dscvit.keats.network

import com.dscvit.keats.model.clubs.ClubsListResponse
import com.dscvit.keats.model.login.LoginRequest
import com.dscvit.keats.model.login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {

    @POST("api/user/")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("api/user/clubs")
    suspend fun getClubs(): Response<ClubsListResponse>
}
