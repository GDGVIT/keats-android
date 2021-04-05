package com.dscvit.keats.model.clubs

import com.squareup.moshi.Json

data class PublicClubsListResponse(
    @Json(name = "data")
    val Clubs: List<ClubEntity>?,
    @Json(name = "status")
    val Status: String
)
