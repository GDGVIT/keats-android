package com.dscvit.keats.ui.clubs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dscvit.keats.R
import com.dscvit.keats.adapter.ClubListAdapter
import com.dscvit.keats.databinding.FragmentClubsListBinding
import com.dscvit.keats.model.Result
import com.dscvit.keats.ui.activities.PostAuthActivity
import com.dscvit.keats.utils.disable
import com.dscvit.keats.utils.enable
import com.dscvit.keats.utils.hide
import com.dscvit.keats.utils.shortToast
import com.dscvit.keats.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubsListFragment : Fragment(), ClubListAdapter.OnClubListener {

    private val viewModel: ClubsListViewModel by viewModels()
    private lateinit var adapter: ClubListAdapter
    private lateinit var binding: FragmentClubsListBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClubsListBinding.inflate(layoutInflater)
        adapter = ClubListAdapter(requireContext(), this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clubsList.adapter = adapter
        getClubs(false)
        binding.swipeContainer.setOnRefreshListener {
            getClubs(true)
            binding.swipeContainer.isRefreshing = false
        }
        binding.swipeContainer.setColorSchemeColors(
            ResourcesCompat.getColor(resources, R.color.orange_200, null)
        )
    }

    private fun getClubs(fromSwipeRefresh: Boolean) {
        viewModel.getClubs().observe(
            viewLifecycleOwner,
            {
                when (it.status) {
                    Result.Status.LOADING -> {
                        if (!fromSwipeRefresh) {
                            binding.progressBar.enable()
                            binding.progressBar.show()
                        }
                    }
                    Result.Status.SUCCESS -> {
                        if (it.data?.Status == "success") {
                            (requireActivity() as PostAuthActivity).setProfilePhoto(it.data.Data.User.ProfilePic)
                            val clubs = it.data.Data.Clubs
                            if (clubs != null) {
                                clubsExistsShowViews()
                                adapter.submitList(clubs)
                            } else {
                                noClubsShowViews()
                                if (fromSwipeRefresh) {
                                    binding.clubsList.disable()
                                    binding.clubsList.hide()
                                }
                            }
                        }
                    }
                    Result.Status.ERROR -> {
                        if (fromSwipeRefresh) {
                            binding.clubsList.disable()
                            binding.clubsList.hide()
                        }
                        errorOccurredShowViews()
                    }
                }
            }
        )
    }

    private fun clubsExistsShowViews() {
        binding.progressBar.hide()
        binding.progressBar.disable()
        binding.clubsList.enable()
        binding.clubsList.show()
        binding.floatingActionCreateClub.show()
        binding.floatingActionCreateClub.enable()
    }

    private fun noClubsShowViews() {
        binding.progressBar.hide()
        binding.progressBar.disable()
        binding.lyingDownReadingSvg.show()
        binding.lyingDownReadingSvg.enable()
        binding.joinFirstBookClubText.show()
        binding.joinFirstBookClubText.enable()
        binding.createClub.show()
        binding.createClub.enable()
        binding.joinClub.show()
        binding.joinClub.enable()
    }

    private fun errorOccurredShowViews() {
        binding.progressBar.hide()
        binding.progressBar.disable()
        binding.clubsList.disable()
        binding.clubsList.hide()
        binding.lyingDownReadingSvg.setImageResource(R.drawable.ic_error_book)
        binding.lyingDownReadingSvg.enable()
        binding.lyingDownReadingSvg.show()
        binding.joinFirstBookClubText.text = getString(R.string.error_text)
        binding.joinFirstBookClubText.enable()
        binding.joinFirstBookClubText.show()
    }

    override fun onClubClick(position: Int) {
        val clubId = adapter.getClubId(position)
        context?.shortToast(clubId)
    }
}
