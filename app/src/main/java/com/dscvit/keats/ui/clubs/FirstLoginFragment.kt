package com.dscvit.keats.ui.clubs

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dscvit.keats.databinding.FragmentFirstLoginBinding
import com.dscvit.keats.model.Result
import com.dscvit.keats.utils.invisible
import com.dscvit.keats.utils.shortToast
import com.dscvit.keats.utils.show
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream

@AndroidEntryPoint
class FirstLoginFragment : Fragment() {
    private lateinit var binding: FragmentFirstLoginBinding
    private val viewModel: FirstLoginViewModel by viewModels()
    private val args: FirstLoginFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {}
        binding.phoneNumberEditText.setText(args.userPhone)
        binding.profilePhoto.alpha = 0.5F
        binding.phoneNumberEditText.setOnClickListener {
            context?.shortToast("Phone number cannot be edited")
        }
        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val body = getMultipartImageFromDevice(result)
                    body?.let {
                        viewModel.userProfileMultipart = it
                    }
                }
            }
        binding.profilePhoto.setOnClickListener {
            pickImage(startForResult)
        }
        binding.continueButton.setOnClickListener {
            updateDetails()
        }
    }

    private fun updateDetails() {
        if (
            binding.nameEditText.text.toString() == "" ||
            binding.emailEditText.text.toString() == "" ||
            binding.bioEditText.text.toString() == ""
        ) {
            context?.shortToast("Enter all the necessary details")
        } else {
            viewModel.updateUserProfile(
                username = binding.nameEditText.text.toString().trim()
                    .toRequestBody("text/plain".toMediaTypeOrNull()),
                email = binding.emailEditText.text.toString().trim()
                    .toRequestBody("text/plain".toMediaTypeOrNull()),
                bio = binding.bioEditText.text.toString().trim()
                    .toRequestBody("text/plain".toMediaTypeOrNull()),
                profilePic = viewModel.userProfileMultipart
            ).observe(
                viewLifecycleOwner,
                {
                    when (it.status) {
                        Result.Status.LOADING -> {
                            binding.continueButton.invisible()
                            binding.continueLoadingBar.show()
                        }
                        Result.Status.SUCCESS -> {
                            findNavController().navigate(
                                FirstLoginFragmentDirections.actionFirstLoginFragmentToClubsListFragment()
                            )
                        }
                        Result.Status.ERROR -> {
                            binding.continueLoadingBar.invisible()
                            binding.continueButton.show()
                        }
                    }
                }
            )
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
}
