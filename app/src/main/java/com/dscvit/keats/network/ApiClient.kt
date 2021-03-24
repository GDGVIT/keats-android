package com.dscvit.keats.network

import com.dscvit.keats.model.login.LoginRequest
import javax.inject.Inject

class ApiClient @Inject constructor(
    private val authApi: ApiInterface
) : BaseApiClient() {
    suspend fun loginUser(loginRequest: LoginRequest) = getResult {
        authApi.loginUser(loginRequest)
    }

    suspend fun getClubs() = getResult {
        authApi.getClubs()
    }
}
