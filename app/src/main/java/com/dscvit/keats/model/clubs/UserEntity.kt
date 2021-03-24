package com.dscvit.keats.model.clubs

import com.squareup.moshi.Json

data class UserEntity(
    @Json(name = "id")
    val UserId: String,
    @Json(name = "username")
    val UserName: String,
    @Json(name = "phone_number")
    val PhoneNumber: String,
    @Json(name = "profile_pic")
    val ProfilePic: String,
    @Json(name = "email")
    val Email: String,
    @Json(name = "bio")
    val UserBio: String
)
