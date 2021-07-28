package com.dscvit.keats.ui.clubs

import android.app.Activity
import android.content.Intent
import android.graphics.Color
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
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dscvit.keats.R
import com.dscvit.keats.databinding.FragmentEditClubBinding
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
class EditClubFragment : Fragment() {
    private lateinit var binding: FragmentEditClubBinding
    private val viewModel: EditClubViewModel by viewModels()
    private val args: EditClubFragmentArgs by navArgs()
    private var clubId = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditClubBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clubImageCardEdit.alpha = 0.5F
        binding.clubImageEdit.alpha = 0.5F
        showClubDetails()
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
        binding.clubImageUploadEdit.setOnClickListener {
            pickImage(startForResultClubPic)
        }

        binding.selectBookButtonEdit.setOnClickListener {
            pickBook(startForResultClubBook)
        }
        binding.editClubButtonEdit.setOnClickListener {
            editClub()
        }
    }

    private fun showClubDetails() {
        clubId = args.clubId
        binding.clubNameEditTextEdit.setText(args.clubName)
        binding.privateToggleEdit.isChecked = args.clubPrivate
        binding.privateToggleEdit.disable()
        val clubImg = binding.clubImageEdit
        val imgUrl =
            args.clubPicUrl.toUri().buildUpon().scheme("https")
                .build()
        val circularProgressDrawable =
            CircularProgressDrawable(requireContext())
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.setColorSchemeColors(
            Color.argb(100, 244, 121, 18)
        )
        circularProgressDrawable.start()
        Glide.with(clubImg.context)
            .load(imgUrl)
            .apply(
                RequestOptions()
                    .placeholder(circularProgressDrawable)
                    .error(R.drawable.ic_broken_image)
            )
            .into(clubImg)
    }

    private fun editClub() {
        val clubName = binding.clubNameEditTextEdit.text.toString().trim()
            .toRequestBody("text/plain".toMediaTypeOrNull())
        val pageSync = 0.toString()
            .toRequestBody("text/plain".toMediaTypeOrNull())
        val clubId = clubId
            .toRequestBody("text/plain".toMediaTypeOrNull())
        viewModel.editClub(
            clubId = clubId,
            clubName = clubName,
            pageSync = pageSync,
            clubBook = viewModel.clubBookMultipart,
            clubPic = viewModel.clubPicMultipart
        ).observe(
            viewLifecycleOwner,
            {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.editClubButtonEdit.invisible()
                        binding.editClubButtonEdit.disable()
                        binding.clubImageUploadEdit.disable()
                        binding.selectBookButtonEdit.disable()
                        binding.editClubProgressBar.show()
                        binding.editClubProgressBar.enable()
                    }
                    Result.Status.SUCCESS -> {
                        if (it.data?.Status == "success") {
                            context?.shortToast("Club Details Changed Successfully")
                            val data = it.data.Data.ClubId
                            findNavController().navigate(
                                EditClubFragmentDirections.actionEditClubFragmentToClubDetailFragment(
                                    data
                                )
                            )
                        }
                    }
                    Result.Status.ERROR -> {
                        context?.shortToast(it.message.toString())
                        binding.editClubButtonEdit.show()
                        binding.editClubButtonEdit.enable()
                        binding.clubImageUploadEdit.enable()
                        binding.selectBookButtonEdit.enable()
                        binding.editClubProgressBar.hide()
                        binding.editClubProgressBar.disable()
                    }
                }
            }
        )
    }

    private fun getMultipartImageFromDevice(result: ActivityResult): MultipartBody.Part? {
        val image = result.data?.dataString
        val imageUri = Uri.parse(image)
        binding.clubImageEdit.setImageURI(imageUri)
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
        binding.bookNameTextEdit.text = getFileName(documentUri)
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
