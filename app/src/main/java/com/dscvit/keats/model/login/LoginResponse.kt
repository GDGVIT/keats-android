package com.dscvit.keats.model.login

import com.squareup.moshi.Json

class LoginResponse(
    @Json(name = "data")
    val Data: LoginResponseData,
    @Json(name = "status")
    val Status: String
)
