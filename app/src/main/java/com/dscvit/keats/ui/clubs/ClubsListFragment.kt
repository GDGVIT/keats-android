package com.dscvit.keats.ui.clubs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dscvit.keats.databinding.FragmentClubsListBinding

class ClubsListFragment : Fragment() {

    private val viewModel: ClubsListViewModel by viewModels()
    private lateinit var binding: FragmentClubsListBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClubsListBinding.inflate(layoutInflater)
        return binding.root
    }
}
