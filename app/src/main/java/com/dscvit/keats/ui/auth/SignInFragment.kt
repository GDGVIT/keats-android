package com.dscvit.keats.ui.auth

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dscvit.keats.databinding.FragmentSignInBinding
import com.dscvit.keats.firebase.FirebaseAuthHelper
import com.dscvit.keats.utils.Constants
import com.dscvit.keats.utils.PreferenceHelper
import com.dscvit.keats.utils.PreferenceHelper.set
import com.dscvit.keats.utils.disable
import com.dscvit.keats.utils.enable
import com.dscvit.keats.utils.hide
import com.dscvit.keats.utils.shortToast
import com.dscvit.keats.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: FragmentSignInBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences: SharedPreferences =
            PreferenceHelper.customPrefs(requireContext(), Constants.PREF_NAME)
        val authHelper = FirebaseAuthHelper(
            viewLifecycleOwner,
            requireContext(),
            requireActivity(),
            viewModel,
            binding,
            null
        )
        binding.sendOtp.setOnClickListener {
            val phone = binding.phoneNumber.text.toString()
            if (phone.length != 10) {
                requireContext().shortToast("Enter a valid phone number")
            } else {
                it.disable()
                it.hide()
                binding.getOtpProgressBar.show()
                binding.getOtpProgressBar.enable()
                authHelper.sendOtp(phone)
                sharedPreferences[Constants.PREF_PHONE_NUMBER] = phone
            }
        }
    }
}
