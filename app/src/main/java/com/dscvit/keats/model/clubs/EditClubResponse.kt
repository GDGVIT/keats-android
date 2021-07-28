package com.dscvit.keats.model.clubs

import com.squareup.moshi.Json

data class EditClubResponse(
    @Json(name = "data")
    val Data: EditClubEntity,
    @Json(name = "status")
    val Status: String,
    @Json(name = "message")
    val Message: String?
)
