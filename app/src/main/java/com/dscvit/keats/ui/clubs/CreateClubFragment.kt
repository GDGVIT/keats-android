package com.dscvit.keats.ui.clubs

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dscvit.keats.databinding.FragmentCreateClubBinding
import com.dscvit.keats.model.Result
import com.dscvit.keats.utils.disable
import com.dscvit.keats.utils.enable
import com.dscvit.keats.utils.hide
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
class CreateClubFragment : Fragment() {

    private lateinit var binding: FragmentCreateClubBinding
    private val viewModel: CreateClubViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateClubBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clubImageCard.alpha = 0.5F
        binding.clubImage.alpha = 0.5F
        val startForResultClubPic =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val body = getMultipartImageFromDevice(result)
                    body?.let {
                        viewModel.clubPicMultipart = it
                    }
                }
            }

        val startForResultClubBook =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val body = getMultipartDocumentFromDevice(result)
                    body?.let {
                        viewModel.clubBookMultipart = it
                    }
                }
            }
        binding.clubImageUpload.setOnClickListener {
            pickImage(startForResultClubPic)
        }

        binding.selectBookButton.setOnClickListener {
            pickBook(startForResultClubBook)
        }
        binding.createClubButton.setOnClickListener {
            when {
                binding.clubNameEditText.text.toString() == "" -> {
                    context?.shortToast("Please enter a club name")
                }
                viewModel.clubBookMultipart == null -> {
                    context?.shortToast("Please select a book")
                }
                viewModel.clubPicMultipart == null -> {
                    context?.shortToast("Please select a club profile photo")
                }
                else -> {
                    createClub()
                }
            }
        }
    }

    private fun createClub() {
        val clubStatus = binding.privateToggle.isChecked.toString().trim()
            .toRequestBody("text/plain".toMediaTypeOrNull())
        val clubName = binding.clubNameEditText.text.toString().trim()
            .toRequestBody("text/plain".toMediaTypeOrNull())
        val pageSync = true.toString()
            .toRequestBody("text/plain".toMediaTypeOrNull())
        viewModel.createClub(
            clubName = clubName,
            clubStatus = clubStatus,
            pageSync = pageSync,
            clubBook = viewModel.clubBookMultipart,
            clubPic = viewModel.clubPicMultipart
        ).observe(
            viewLifecycleOwner,
            {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.createClubButton.invisible()
                        binding.createClubButton.disable()
                        binding.clubImageUpload.disable()
                        binding.selectBookButton.disable()
                        binding.createClubProgressBar.show()
                        binding.createClubProgressBar.enable()
                    }
                    Result.Status.SUCCESS -> {
                        if (it.data?.Status == "success") {
                            context?.shortToast("Club Created")
                            val data = it.data.Data.ClubId
                            findNavController().navigate(
                                CreateClubFragmentDirections.actionCreateClubFragmentToClubDetailFragment(
                                    data
                                )
                            )
                        }
                    }
                    Result.Status.ERROR -> {
                        context?.shortToast(it.message.toString())
                        binding.createClubButton.show()
                        binding.createClubButton.enable()
                        binding.clubImageUpload.enable()
                        binding.selectBookButton.enable()
                        binding.createClubProgressBar.hide()
                        binding.createClubProgressBar.disable()
                    }
                }
            }
        )
    }

    private fun getMultipartImageFromDevice(result: ActivityResult): MultipartBody.Part? {
        val image = result.data?.dataString
        val imageUri = Uri.parse(image)
        binding.clubImage.setImageURI(imageUri)
        val inputStream: InputStream? =
            (requireActivity()).contentResolver.openInputStream(imageUri)
        val reqFile: RequestBody? =
            inputStream?.readBytes()?.toRequestBody(result.data?.type?.toMediaTypeOrNull())
        return reqFile?.let {
            MultipartBody.Part.createFormData(
                "club_pic",
                "file.${result.data?.type?.split("/")?.get(1)}",
                it
            )
        }
    }

    private fun getMultipartDocumentFromDevice(result: ActivityResult): MultipartBody.Part? {
        val document = result.data?.dataString
        val documentUri = Uri.parse(document)
        binding.bookNameText.text = getFileName(documentUri)
        val inputStream: InputStream? =
            (requireActivity()).contentResolver.openInputStream(documentUri)
        val reqFile: RequestBody? =
            inputStream?.readBytes()?.toRequestBody(result.data?.type?.toMediaTypeOrNull())
        return reqFile?.let {
            MultipartBody.Part.createFormData(
                "file",
                "file.${result.data?.type?.split("/")?.get(1)}",
                it
            )
        }
    }

    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context?.contentResolver?.query(uri, null, null, null, null)
            cursor.use {
                if (it != null) {
                    if (it.moveToFirst()) {
                        result = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result!!.substring(cut + 1)
            }
        }
        return result
    }

    private fun pickImage(startForResult: ActivityResultLauncher<Intent>) {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startForResult.launch(galleryIntent)
    }

    private fun pickBook(startForResult: ActivityResultLauncher<Intent>) {
        val mimeTypes = arrayOf("application/epub", "application/pdf")
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
            .setType("application/epub|application/pdf")
            .putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startForResult.launch(pdfIntent)
    }
}
