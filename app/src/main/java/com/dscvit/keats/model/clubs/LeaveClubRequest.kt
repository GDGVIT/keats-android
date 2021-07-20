package com.dscvit.keats.model.clubs

import com.squareup.moshi.Json

data class LeaveClubRequest(
    @Json(name = "club_id")
    val ClubId: String,
)
