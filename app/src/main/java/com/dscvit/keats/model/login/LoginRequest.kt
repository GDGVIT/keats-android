package com.dscvit.keats.model.login

import com.squareup.moshi.Json

data class LoginRequest(
    @Json(name = "id_token")
    val IdToken: String
)
