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
        binding.sendOtp.setOnClickListener {
//            viewModel.sendOtp(binding.phoneNumber)
            val authHelper = FirebaseAuthHelper(requireContext(), requireActivity())
            authHelper.sendOtp(binding.phoneNumber)
            sharedPreferences[Constants.PREF_PHONE_NUMBER] = binding.phoneNumber.text.toString()
        }
    }
}
