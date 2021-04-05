package com.dscvit.keats.model.profile

import com.squareup.moshi.Json

data class UpdateUserResponse(
    @Json(name = "data")
    val User: UserEntity,
    @Json(name = "status")
    val Status: String
)
