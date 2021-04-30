package com.dscvit.keats.ui.clubs

import androidx.lifecycle.ViewModel
import com.dscvit.keats.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class CreateClubViewModel @Inject constructor(private val repo: AppRepository) : ViewModel() {
    fun uploadFile(file: MultipartBody.Part) = repo.uploadFile(file)
}
