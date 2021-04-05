package com.dscvit.keats.ui.clubs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.dscvit.keats.databinding.FragmentScanQrCodeBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ScanQRCodeFragment : Fragment() {

    private lateinit var binding: FragmentScanQrCodeBinding
    private lateinit var codeScanner: CodeScanner
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
    }
}
