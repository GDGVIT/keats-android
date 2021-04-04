package com.dscvit.keats.model.profile

import com.squareup.moshi.Json

data class UpdateUserRequest(
    @Json(name = "username")
    val UserName: String,
    @Json(name = "email")
    val UserEmail: String,
    @Json(name = "bio")
    val UserBio: String
)
