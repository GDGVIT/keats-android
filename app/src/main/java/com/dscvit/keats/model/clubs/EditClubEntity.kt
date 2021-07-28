package com.dscvit.keats.model.clubs

import com.squareup.moshi.Json

data class EditClubEntity(
    @Json(name = "id")
    val ClubId: String,
    @Json(name = "clubname")
    val ClubName: String,
    @Json(name = "file_url")
    val FileUrl: String,
    @Json(name = "club_pic")
    val ClubPic: String,
    @Json(name = "page_no")
    val PageNo: String,
    @Json(name = "private")
    val Private: Boolean,
    @Json(name = "page_sync")
    val PageSync: Boolean,
    @Json(name = "host_id")
    val HostId: String
)
