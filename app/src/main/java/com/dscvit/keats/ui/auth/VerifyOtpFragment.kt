package com.dscvit.keats.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dscvit.keats.databinding.FragmentVerifyOtpBinding
import com.dscvit.keats.firebase.FirebaseAuthHelper
import com.dscvit.keats.utils.Constants
import com.dscvit.keats.utils.PreferenceHelper
import com.dscvit.keats.utils.disable
import com.dscvit.keats.utils.enable
import com.dscvit.keats.utils.hide
import com.dscvit.keats.utils.shortToast
import com.dscvit.keats.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyOtpFragment : Fragment() {
    private val viewModel: AuthViewModel by viewModels()
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
        val authHelper = FirebaseAuthHelper(
            viewLifecycleOwner,
            requireContext(),
            requireActivity(),
            viewModel,
            null,
            binding
        )
        binding.verifyOtp.setOnClickListener {
            if (binding.sentOtp.text?.length == 6) {
                binding.verifyOtp.disable()
                binding.verifyOtp.hide()
                binding.verifyOtpProgressBar.enable()
                binding.verifyOtpProgressBar.show()
                authHelper.authenticate(binding.sentOtp.text.toString())
            } else {
                requireContext().shortToast("Enter a six digit OTP")
            }
        }
    }
}
