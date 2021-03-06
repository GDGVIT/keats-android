package com.dscvit.keats.ui.profile

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
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
import com.dscvit.keats.utils.invisible
import com.dscvit.keats.utils.shortToast
import com.dscvit.keats.utils.show
import com.dscvit.keats.utils.validateEmail
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.InputStream

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private val viewModel: UserProfileViewModel by viewModels()
    private lateinit var binding: FragmentUserProfileBinding
    private var userProfileUrl = ""
    private var userProfileMultipart: MultipartBody.Part? = null
    private lateinit var openAnimation: Animation
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileBinding.inflate(layoutInflater)
        openAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.open_anim)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserprofile()
        binding.logoutButton.setOnClickListener {
            logout()
        }
        binding.startEdit.setOnClickListener {
            startEdit()
        }
        binding.endEdit.setOnClickListener {
            if (!validateEmail(binding.emailEditText.text.toString().trim()) &&
                binding.emailEditText.text.toString().trim() != ""
            ) {
                context?.shortToast("Please enter a valid email id!")
            } else {
                updateDetails()
            }
        }
        binding.phoneNumberEditText.setOnClickListener {
            context?.shortToast("Phone number cannot be edited")
        }
        binding.backButton.setOnClickListener {
            activity?.finish()
        }
        binding.cancelEdit.setOnClickListener {
            cancelEdit()
        }
        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val body = getMultipartImageFromDevice(result)
                    body?.let {
                        userProfileMultipart = it
                    }
                }
            }
        binding.profilePhoto.setOnClickListener {
            pickImage(startForResult)
        }
    }

    private fun getMultipartImageFromDevice(result: ActivityResult): MultipartBody.Part? {
        val image = result.data?.dataString
        val imageUri = Uri.parse(image)
        binding.profilePhoto.setImageURI(imageUri)
        val inputStream: InputStream? =
            (requireActivity()).contentResolver.openInputStream(imageUri)
        val reqFile: RequestBody? =
            inputStream?.readBytes()?.toRequestBody(result.data?.type?.toMediaTypeOrNull())
        return reqFile?.let {
            MultipartBody.Part.createFormData(
                "profile_pic",
                "file.${result.data?.type?.split("/")?.get(1)}",
                it
            )
        }
    }

    private fun pickImage(startForResult: ActivityResultLauncher<Intent>) {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startForResult.launch(galleryIntent)
    }

    private fun cancelEdit() {
        binding.endEdit.hide()
        binding.endEdit.disable()
        binding.profilePhoto.isClickable = false
        binding.profilePhoto.alpha = 1.0F
        binding.profilePhotoUpload.hide()
        binding.profilePhotoUpload.disable()
        binding.startEdit.show()
        binding.startEdit.enable()
        binding.cancelEdit.hide()
        binding.cancelEdit.disable()
        binding.nameEditText.setText(binding.userName.text)
        binding.bioEditText.setText(binding.userBio.text)
        binding.emailEditText.setText(binding.userEmail.text)
        binding.phoneNumberEditText.setText(binding.userPhone.text)
        hideEditTexts()
        showTextViews()
    }

    private fun loadUserprofile() {
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
                        Timber.e("Error is: ${it.message}")
                        activity?.finish()
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

    private fun updateDetails() {
        viewModel.updateUserProfile(
            username = binding.nameEditText.text.toString().trim()
                .toRequestBody("text/plain".toMediaTypeOrNull()),
            email = binding.emailEditText.text.toString().trim()
                .toRequestBody("text/plain".toMediaTypeOrNull()),
            bio = binding.bioEditText.text.toString().trim()
                .toRequestBody("text/plain".toMediaTypeOrNull()),
            profilePic = userProfileMultipart
        ).observe(
            viewLifecycleOwner,
            {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.endEdit.hide()
                        binding.endEdit.disable()
                        binding.cancelEdit.hide()
                        binding.cancelEdit.disable()
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
                            val profilePicImg = binding.profilePhoto
                            val imgUri =
                                it.data.User.ProfilePic.toUri().buildUpon().scheme("https").build()
                            val circularProgressDrawable =
                                CircularProgressDrawable(requireContext())
                            circularProgressDrawable.strokeWidth = 5f
                            circularProgressDrawable.centerRadius = 30f
                            circularProgressDrawable.setColorSchemeColors(
                                Color.argb(100, 244, 121, 18)
                            )
                            circularProgressDrawable.start()
                            Glide.with(profilePicImg.context)
                                .load(imgUri)
                                .apply(
                                    RequestOptions()
                                        .placeholder(circularProgressDrawable)
                                        .error(R.drawable.ic_default_photo)
                                )
                                .into(profilePicImg)
                            endEditViews()
                        }
                    }
                    Result.Status.ERROR -> {
                        Timber.e("Error is: ${it.message}")
                        context?.shortToast("Error in updating! Retry again")
                        binding.updatingProfileProgressBar.hide()
                        binding.updatingProfileProgressBar.disable()
                    }
                }
            }
        )
    }

    private fun endEditViews() {
        showTextViews()
        binding.profilePhoto.isClickable = false
        binding.profilePhoto.alpha = 1.0F
        binding.profilePhotoUpload.hide()
        binding.profilePhotoUpload.disable()
        binding.startEdit.show()
        binding.startEdit.enable()
        binding.cancelEdit.hide()
        binding.cancelEdit.disable()
        binding.updatingProfileProgressBar.hide()
        binding.updatingProfileProgressBar.disable()
        hideEditTexts()
    }

    private fun startEdit() {
        hideTextViews()
        binding.startEdit.hide()
        binding.startEdit.disable()
        binding.endEdit.show()
        binding.endEdit.enable()
        binding.cancelEdit.show()
        binding.cancelEdit.enable()
        showEditTexts()
        binding.profilePhoto.isClickable = true
        binding.profilePhoto.alpha = 0.5F
        binding.profilePhotoUpload.show()
        binding.profilePhotoUpload.enable()
    }

    private fun showUserProfileViews(user: UserEntity) {
        binding.progressBar.hide()
        binding.progressBar.disable()
        binding.backButton.show()
        binding.backButton.enable()
        binding.logoutButton.startAnimation(openAnimation)
        binding.logoutButton.show()
        binding.logoutButton.enable()
        binding.coverPhoto.startAnimation(openAnimation)
        binding.coverPhoto.show()
        binding.coverPhoto.enable()
        binding.profilePhoto.startAnimation(openAnimation)
        binding.profilePhoto.show()
        binding.profilePhoto.enable()
        binding.userInfoCard.startAnimation(openAnimation)
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
        userProfileUrl = user.ProfilePic
        val profilePicImg = binding.profilePhoto
        val imgUri = userProfileUrl.toUri().buildUpon().scheme("https").build()
        val circularProgressDrawable = CircularProgressDrawable(requireContext())
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.setColorSchemeColors(
            Color.argb(100, 244, 121, 18)
        )
        circularProgressDrawable.start()
        Glide.with(profilePicImg.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(circularProgressDrawable)
                    .error(R.drawable.ic_default_photo)
            )
            .into(profilePicImg)
        profilePicImg.isClickable = false
    }

    private fun showEditTexts() {
        binding.nameEditText.show()
        binding.nameEditText.enable()
        binding.bioEditText.show()
        binding.bioEditText.enable()
        binding.emailEditText.show()
        binding.emailEditText.enable()
        binding.phoneNumberEditText.show()
        binding.phoneNumberEditText.enable()
    }

    private fun hideEditTexts() {
        binding.nameEditText.invisible()
        binding.nameEditText.disable()
        binding.bioEditText.invisible()
        binding.bioEditText.disable()
        binding.emailEditText.invisible()
        binding.emailEditText.disable()
        binding.phoneNumberEditText.invisible()
        binding.phoneNumberEditText.disable()
    }

    private fun showTextViews() {
        binding.userName.show()
        binding.userName.enable()
        binding.userBio.show()
        binding.userBio.enable()
        binding.userEmail.show()
        binding.userEmail.enable()
        binding.userPhone.show()
        binding.userPhone.enable()
    }

    private fun hideTextViews() {
        binding.userName.hide()
        binding.userName.disable()
        binding.userBio.hide()
        binding.userBio.disable()
        binding.userEmail.hide()
        binding.userEmail.disable()
        binding.userPhone.hide()
        binding.userPhone.disable()
    }
}
