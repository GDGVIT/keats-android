package com.dscvit.keats.model.clubs

import com.squareup.moshi.Json

data class KickMemberRequest(
    @Json(name = "club_id")
    val ClubId: String,
    @Json(name = "user_id")
    val UserId: String
)
