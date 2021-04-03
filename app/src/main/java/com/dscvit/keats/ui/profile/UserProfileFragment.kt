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
import com.dscvit.keats.model.profile.UserEntity
import com.dscvit.keats.ui.activities.PreAuthActivity
import com.dscvit.keats.utils.Constants
import com.dscvit.keats.utils.PreferenceHelper
import com.dscvit.keats.utils.disable
import com.dscvit.keats.utils.enable
import com.dscvit.keats.utils.hide
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
