package com.dscvit.keats.ui.clubs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dscvit.keats.databinding.FragmentCreateClubBinding
import dagger.hilt.android.AndroidEntryPoint

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
}
