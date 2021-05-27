package com.dscvit.keats.model.clubs

import com.squareup.moshi.Json

data class CreateClubResponse(
    @Json(name = "data")
    val Data: CreateClubEntity,
    @Json(name = "status")
    val Status: String,
)
