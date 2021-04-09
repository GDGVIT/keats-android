package com.dscvit.keats.model.clubs

import com.dscvit.keats.model.profile.UserEntity
import com.squareup.moshi.Json

data class GetClubDetailsData(
    @Json(name = "club")
    val Club: ClubEntity,
    @Json(name = "users")
    val Users: List<UserEntity>
)
