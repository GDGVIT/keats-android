package com.dscvit.keats.ui.clubs

import androidx.lifecycle.ViewModel
import com.dscvit.keats.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class JoinClubViewModel @Inject constructor(private val repo: AppRepository) : ViewModel() {
    fun getPublicClubsList() = repo.getPublicClubsList()
}
