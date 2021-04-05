package com.dscvit.keats.model.clubs

import com.squareup.moshi.Json

data class JoinClubResponse(
    @Json(name = "message")
    val Message: String,
    @Json(name = "status")
    val Status: String
)
