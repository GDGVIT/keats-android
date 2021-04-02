package com.dscvit.keats.model.clubs

import com.squareup.moshi.Json

data class ClubsListResponse(
    @Json(name = "status")
    val Status: String,
    @Json(name = "data")
    val Data: ClubListResponseData
)
