package com.dscvit.keats.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.dscvit.keats.databinding.FragmentVerifyOtpBinding
import com.dscvit.keats.firebase.AuthHelper
import com.dscvit.keats.utils.Constants
import com.dscvit.keats.utils.PreferenceHelper
import com.dscvit.keats.utils.shortToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyOtpFragment : Fragment() {
    private lateinit var binding: FragmentVerifyOtpBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerifyOtpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {}
        PreferenceHelper.customPrefs(requireContext(), Constants.PREF_NAME)
        binding.verifyOtp.setOnClickListener {
            if (binding.sentOtp.text?.length == 6) {
                val authHelper = AuthHelper(requireContext(), view, requireActivity())
                val token = authHelper.authenticate(binding.sentOtp).toString()
                Log.i("I/ID_TOKEN", token)
            } else {
                requireContext().shortToast("Enter a six digit OTP")
            }
        }
    }
}
