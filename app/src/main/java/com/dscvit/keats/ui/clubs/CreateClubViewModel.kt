package com.dscvit.keats.ui.clubs

import androidx.lifecycle.ViewModel
import com.dscvit.keats.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class CreateClubViewModel @Inject constructor(private val repo: AppRepository) : ViewModel() {

    var clubPicMultipart: MultipartBody.Part? = null
    var clubBookMultipart: MultipartBody.Part? = null

    fun createClub(
        clubName: RequestBody,
        clubStatus: RequestBody,
        pageSync: RequestBody,
        clubPic: MultipartBody.Part?,
        clubBook: MultipartBody.Part?
    ) = repo.createClub(
        clubName = clubName,
        clubStatus = clubStatus,
        pageSync = pageSync,
        clubPic = clubPic,
        clubBook = clubBook
    )
}
