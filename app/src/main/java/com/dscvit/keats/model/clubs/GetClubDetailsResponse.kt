package com.dscvit.keats.model.clubs

import com.squareup.moshi.Json

data class GetClubDetailsResponse(
    @Json(name = "status")
    val Status: String,
    @Json(name = "data")
    val Data: GetClubDetailsData
)
