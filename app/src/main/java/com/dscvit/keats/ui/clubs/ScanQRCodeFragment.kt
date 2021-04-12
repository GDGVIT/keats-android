package com.dscvit.keats.ui.clubs

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.dscvit.keats.databinding.FragmentScanQrCodeBinding
import com.dscvit.keats.utils.shortToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.InputStream

@AndroidEntryPoint
class ScanQRCodeFragment : Fragment() {

    private lateinit var binding: FragmentScanQrCodeBinding
    private lateinit var codeScanner: CodeScanner
    private val viewModel: JoinClubViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScanQrCodeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scannerView = binding.qrCodeScanner
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                Timber.i("The text is:${it.text}")
                codeScanner.stopPreview()
                val action =
                    ScanQRCodeFragmentDirections.actionScanQRCodeFragmentToJoinClubFragment(it.text)
                findNavController().navigate(action)
            }
        }
        codeScanner.startPreview()

        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                codeScanner.startPreview()
                if (result.resultCode == Activity.RESULT_OK) {
                    val image = result.data?.dataString
                    val imageUri = Uri.parse(image)
                    val inputStream: InputStream? =
                        activity.contentResolver.openInputStream(imageUri)
                    decodeImage(inputStream)
                }
            }

        binding.pickQrCode.setOnClickListener {
            pickImage(startForResult)
        }
    }

    private fun pickImage(startForResult: ActivityResultLauncher<Intent>) {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startForResult.launch(galleryIntent)
    }

    private fun decodeImage(inputStream: InputStream?) {
        val action: NavDirections? = if (inputStream == null) {
            context?.shortToast("No Image Selected")
            ScanQRCodeFragmentDirections.actionScanQRCodeFragmentToJoinClubFragment()
        } else {
            val decodedClubId = viewModel.decodeQR(inputStream)
            if (decodedClubId != "Not a QR Code") {
                ScanQRCodeFragmentDirections.actionScanQRCodeFragmentToJoinClubFragment(
                    decodedClubId
                )
            } else {
                context?.shortToast(decodedClubId)
                null
            }
        }
        if (action != null) {
            findNavController().navigate(action)
        }
    }
}
