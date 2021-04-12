package com.dscvit.keats.model.clubs

import com.squareup.moshi.Json

data class UploadFileResponse(
    @Json(name = "data")
    val File: String,
    @Json(name = "status")
    val Status: String
)
