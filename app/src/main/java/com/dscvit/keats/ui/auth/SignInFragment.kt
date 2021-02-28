package com.dscvit.keats.ui.auth

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dscvit.keats.databinding.FragmentSignInBinding
import com.dscvit.keats.firebase.AuthHelper
import com.dscvit.keats.utils.Constants
import com.dscvit.keats.utils.PreferenceHelper
import com.dscvit.keats.utils.PreferenceHelper.set
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

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
        binding.sendOtp.setOnClickListener {
            val authHelper = AuthHelper(requireContext(), view, requireActivity())
            authHelper.sendOtp(binding.phoneNumber)
            sharedPreferences[Constants.PREF_PHONE_NUMBER] = binding.phoneNumber.text.toString()
        }
    }
}
