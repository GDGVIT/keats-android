package com.dscvit.keats.ui.clubs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dscvit.keats.R
import com.dscvit.keats.adapter.ClubListAdapter
import com.dscvit.keats.databinding.FragmentClubsListBinding
import com.dscvit.keats.model.Result
import com.dscvit.keats.ui.activities.PostAuthActivity
import com.dscvit.keats.utils.disable
import com.dscvit.keats.utils.enable
import com.dscvit.keats.utils.hide
import com.dscvit.keats.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubsListFragment : Fragment(), ClubListAdapter.OnClubListener {

    private val viewModel: ClubsListViewModel by viewModels()
    private lateinit var adapter: ClubListAdapter
    private lateinit var binding: FragmentClubsListBinding
    private lateinit var fabOpenAnimation: Animation
    private lateinit var fabCloseAnimation: Animation
    private var isFabMenuOpen = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClubsListBinding.inflate(layoutInflater)
        adapter = ClubListAdapter(requireContext(), this)
        fabOpenAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.open_fab_anim)
        fabCloseAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.close_fab_anim)
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
        binding.joinClub.setOnClickListener {
            joinClub()
        }
        binding.floatingActionCreateOrJoinClub.setOnClickListener {
            if (isFabMenuOpen)
                collapseFabMenu()
            else
                expandFabMenu()
        }
        binding.floatingActionJoinClub.setOnClickListener {
            joinClub()
        }
    }

    private fun joinClub() {
        if (isFabMenuOpen) {
            collapseFabMenu()
        }
        val action = ClubsListFragmentDirections.actionClubsListFragmentToJoinClubFragment()
        findNavController().navigate(action)
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
        binding.floatingActionCreateOrJoinClub.show()
        binding.floatingActionCreateOrJoinClub.enable()
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
        if (isFabMenuOpen) {
            return
        }
        val action = ClubsListFragmentDirections.actionClubsListFragmentToClubDetailFragment(clubId)
        findNavController().navigate(action)
    }

    private fun expandFabMenu() {
        ViewCompat.animate(binding.floatingActionCreateOrJoinClub).rotation(45.0f).withLayer()
            .setDuration(300).setInterpolator(OvershootInterpolator(10.0f)).start()
        binding.createClubLayout.startAnimation(fabOpenAnimation)
        binding.joinClubLayout.startAnimation(fabOpenAnimation)
        binding.floatingActionCreateClub.isClickable = true
        binding.floatingActionJoinClub.isClickable = true
        isFabMenuOpen = true
    }

    private fun collapseFabMenu() {
        ViewCompat.animate(binding.floatingActionCreateOrJoinClub).rotation(0.0f).withLayer()
            .setDuration(300).setInterpolator(OvershootInterpolator(10.0f)).start()
        binding.createClubLayout.startAnimation(fabCloseAnimation)
        binding.joinClubLayout.startAnimation(fabCloseAnimation)
        binding.floatingActionCreateClub.isClickable = false
        binding.floatingActionJoinClub.isClickable = false
        isFabMenuOpen = false
    }
}
