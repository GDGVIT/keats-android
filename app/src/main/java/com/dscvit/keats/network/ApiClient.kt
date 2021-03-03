package com.dscvit.keats.network

import com.dscvit.keats.model.login.LoginRequest
import javax.inject.Inject

class ApiClient @Inject constructor(
    private val preAuthApi: ApiInterface
) : BaseApiClient() {
    suspend fun loginUser(loginRequest: LoginRequest) = getResult {
        preAuthApi.loginUser(loginRequest)
    }
}
