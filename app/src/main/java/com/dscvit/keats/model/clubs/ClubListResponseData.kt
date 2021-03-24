package com.dscvit.keats.model.clubs

import com.squareup.moshi.Json

data class ClubListResponseData(
    @Json(name = "clubs")
    val Clubs: List<ClubEntity>?,
    @Json(name = "user")
    val User: UserEntity
)
