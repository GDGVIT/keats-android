package com.dscvit.keats.ui.profile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dscvit.keats.R
import com.dscvit.keats.databinding.FragmentUserProfileBinding
import com.dscvit.keats.model.Result
import com.dscvit.keats.model.profile.UpdateUserRequest
import com.dscvit.keats.model.profile.UserEntity
import com.dscvit.keats.ui.activities.PreAuthActivity
import com.dscvit.keats.utils.Constants
import com.dscvit.keats.utils.PreferenceHelper
import com.dscvit.keats.utils.disable
import com.dscvit.keats.utils.enable
import com.dscvit.keats.utils.hide
import com.dscvit.keats.utils.invisible
import com.dscvit.keats.utils.shortToast
import com.dscvit.keats.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private val viewModel: UserProfileViewModel by viewModels()
    private lateinit var binding: FragmentUserProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logoutButton.setOnClickListener {
            logout()
        }
        viewModel.getUserProfile().observe(
            viewLifecycleOwner,
            {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                        binding.progressBar.enable()
                    }
                    Result.Status.SUCCESS -> {
                        if (it.data?.Status == "success") {
                            showUserProfileViews(it.data.User)
                        }
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        binding.progressBar.disable()
                    }
                }
            }
        )
        binding.startEdit.setOnClickListener {
            startEdit()
        }
        binding.endEdit.setOnClickListener {
            updateDetails()
        }
        binding.phoneNumberEditText.setOnClickListener {
            context?.shortToast("Phone number cannot be edited")
        }
    }

    private fun logout() {
        val sharedPreferences: SharedPreferences =
            PreferenceHelper.customPrefs(requireContext(), Constants.PREF_NAME)
        sharedPreferences.edit {
            remove(Constants.PREF_AUTH_KEY)
            remove(Constants.PREF_IS_LOGGED_IN)
            commit()
        }
        val intent = Intent(requireActivity(), PreAuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        requireActivity().startActivity(intent)
        requireActivity().finishAffinity()
        context?.shortToast("Successfully Logged Out")
    }

    private fun updateDetails() {
        val updateUserRequest = UpdateUserRequest(
            UserName = binding.nameEditText.text.toString(),
            UserEmail = binding.emailEditText.text.toString(),
            UserBio = binding.bioEditText.text.toString(),
        )
        viewModel.updateUserProfile(updateUserRequest).observe(
            viewLifecycleOwner,
            {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.endEdit.hide()
                        binding.endEdit.disable()
                        binding.updatingProfileProgressBar.show()
                        binding.updatingProfileProgressBar.enable()
                    }
                    Result.Status.SUCCESS -> {
                        if (it.data?.Status == "success") {
                            context?.shortToast("Updated Details Successfully")
                            binding.userName.text = it.data.User.UserName
                            binding.userBio.text = it.data.User.UserBio
                            binding.userEmail.text = it.data.User.Email
                            binding.userPhone.text = it.data.User.PhoneNumber
                            endEditViews()
                        }
                    }
                    Result.Status.ERROR -> {
                        context?.shortToast("Error in updating! Retry again")
                        binding.updatingProfileProgressBar.hide()
                        binding.updatingProfileProgressBar.disable()
                    }
                }
            }
        )
    }

    private fun endEditViews() {
        binding.userName.show()
        binding.userName.enable()
        binding.userBio.show()
        binding.userBio.enable()
        binding.userEmail.show()
        binding.userEmail.enable()
        binding.userPhone.show()
        binding.userPhone.enable()
        binding.startEdit.show()
        binding.startEdit.enable()
        binding.updatingProfileProgressBar.hide()
        binding.updatingProfileProgressBar.disable()
        binding.nameEditText.invisible()
        binding.nameEditText.disable()
        binding.bioEditText.invisible()
        binding.bioEditText.disable()
        binding.emailEditText.invisible()
        binding.emailEditText.disable()
        binding.phoneNumberEditText.invisible()
        binding.phoneNumberEditText.disable()
    }

    private fun startEdit() {
        binding.userName.hide()
        binding.userName.disable()
        binding.userBio.hide()
        binding.userBio.disable()
        binding.userEmail.hide()
        binding.userEmail.disable()
        binding.userPhone.hide()
        binding.userPhone.disable()
        binding.startEdit.hide()
        binding.startEdit.disable()
        binding.endEdit.show()
        binding.endEdit.enable()
        binding.nameEditText.show()
        binding.nameEditText.enable()
        binding.bioEditText.show()
        binding.bioEditText.enable()
        binding.emailEditText.show()
        binding.emailEditText.enable()
        binding.phoneNumberEditText.show()
        binding.phoneNumberEditText.enable()
    }

    private fun showUserProfileViews(user: UserEntity) {
        binding.progressBar.hide()
        binding.progressBar.disable()
        binding.logoutButton.show()
        binding.logoutButton.enable()
        binding.coverPhoto.show()
        binding.coverPhoto.enable()
        binding.profilePhoto.show()
        binding.profilePhoto.enable()
        binding.userInfoCard.show()
        binding.userInfoCard.enable()
        binding.coverPhoto.show()
        binding.coverPhoto.enable()
        binding.userName.text = user.UserName
        binding.userBio.text = user.UserBio
        binding.userEmail.text = user.Email
        binding.userPhone.text = user.PhoneNumber
        binding.nameEditText.setText(user.UserName)
        binding.bioEditText.setText(user.UserBio)
        binding.emailEditText.setText(user.Email)
        binding.phoneNumberEditText.setText(user.PhoneNumber)
        val profilePicImg = binding.profilePhoto
        val imgUri = user.ProfilePic.toUri().buildUpon().scheme("https").build()
        Glide.with(profilePicImg.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_default_photo)
                    .error(R.drawable.ic_broken_image)
            )
            .into(profilePicImg)
    }
}
