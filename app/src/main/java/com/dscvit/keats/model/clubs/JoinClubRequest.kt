package com.dscvit.keats.model.clubs

import com.squareup.moshi.Json

data class JoinClubRequest(
    @Json(name = "club_id")
    val ClubId: String
)
