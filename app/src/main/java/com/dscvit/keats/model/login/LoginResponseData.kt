package com.dscvit.keats.model.login

import com.squareup.moshi.Json

data class LoginResponseData(
    @Json(name = "token")
    val Token: String,
    @Json(name = "user_id")
    val UserId: String
)
