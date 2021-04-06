package com.dscvit.keats.ui.clubs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dscvit.keats.adapter.ClubListAdapter
import com.dscvit.keats.databinding.FragmentJoinClubBinding
import com.dscvit.keats.model.Result
import com.dscvit.keats.model.clubs.JoinClubRequest
import com.dscvit.keats.utils.disable
import com.dscvit.keats.utils.enable
import com.dscvit.keats.utils.hide
import com.dscvit.keats.utils.shortToast
import com.dscvit.keats.utils.show
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class JoinClubFragment : Fragment(), ClubListAdapter.OnClubListener {

    private lateinit var binding: FragmentJoinClubBinding
    private val viewModel: JoinClubViewModel by viewModels()
    private val args: JoinClubFragmentArgs by navArgs()
    private lateinit var adapter: ClubListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJoinClubBinding.inflate(layoutInflater)
        adapter = ClubListAdapter(requireContext(), this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.scanCode.setOnClickListener {
            findNavController().navigate(JoinClubFragmentDirections.actionJoinClubFragmentToScanQRCodeFragment())
        }
        if (args.scannedClubId != "Enter Club ID") {
            binding.clubIdEditText.setText(args.scannedClubId)
        }
        binding.joinClubButton.setOnClickListener {
            val clubId = binding.clubIdEditText.text.toString()
            joinClub(clubId)
        }
        binding.publicClubsList.adapter = adapter
        getPublicClubs()
    }

    private fun joinClub(clubId: String) {
        binding.joinClubButton.hide()
        binding.joinClubButton.disable()
        val joinClubRequest = JoinClubRequest(ClubId = clubId)
        viewModel.joinClub(joinClubRequest).observe(
            viewLifecycleOwner,
            {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.joinClubProgressBar.show()
                        binding.joinClubProgressBar.enable()
                    }
                    Result.Status.SUCCESS -> {
                        if (it.data?.Status == "success") {
                            context?.shortToast(it.data.Message)
                            findNavController().navigate(JoinClubFragmentDirections.actionJoinClubFragmentToClubsListFragment())
                        }
                    }
                    Result.Status.ERROR -> {
                        context?.shortToast(it.message.toString())
                        Timber.e("Error is: ${it.message}")
                        binding.joinClubProgressBar.hide()
                        binding.joinClubProgressBar.disable()
                        binding.joinClubButton.show()
                        binding.joinClubButton.enable()
                    }
                }
            }
        )
    }

    private fun getPublicClubs() {
        viewModel.getPublicClubsList().observe(
            viewLifecycleOwner,
            {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.publicClubsListProgressBar.show()
                        binding.publicClubsListProgressBar.enable()
                    }
                    Result.Status.SUCCESS -> {
                        binding.publicClubsListProgressBar.hide()
                        binding.publicClubsListProgressBar.disable()
                        if (it.data?.Status == "success") {
                            val clubs = it.data.Clubs
                            if (clubs != null) {
                                adapter.submitList(clubs)
                            }
                            binding.publicClubsList.show()
                            binding.publicClubsList.enable()
                        }
                    }
                    Result.Status.ERROR -> {
                        val status = it.message.toString()
                        if (status == "404 Not Found") {
                            binding.noClubsText.show()
                            binding.noClubsText.enable()
                        }
                        binding.publicClubsListProgressBar.hide()
                        binding.publicClubsListProgressBar.disable()
                    }
                }
            }
        )
    }

    override fun onClubClick(position: Int) {
        val clubId = adapter.getClubId(position)
        binding.clubIdEditText.setText(clubId)
    }
}
