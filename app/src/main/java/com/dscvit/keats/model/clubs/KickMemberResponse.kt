package com.dscvit.keats.model.clubs

import com.squareup.moshi.Json

data class KickMemberResponse(
    @Json(name = "message")
    val Message: String,
    @Json(name = "status")
    val Status: String
)
