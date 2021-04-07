package com.dscvit.keats.ui.clubs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dscvit.keats.R
import com.dscvit.keats.databinding.FragmentClubDetailBinding
import com.dscvit.keats.databinding.QrCodeDialogBinding
import com.dscvit.keats.model.Result
import com.dscvit.keats.utils.disable
import com.dscvit.keats.utils.enable
import com.dscvit.keats.utils.hide
import com.dscvit.keats.utils.longToast
import com.dscvit.keats.utils.show
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubDetailFragment : Fragment() {

    private lateinit var binding: FragmentClubDetailBinding
    private val viewModel: ClubDetailViewModel by viewModels()
    private val args: ClubDetailFragmentArgs by navArgs()
    private lateinit var fabOpenAnimation: Animation
    private lateinit var fabCloseAnimation: Animation
    private lateinit var openAnimation: Animation
    private var isFabMenuOpen = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClubDetailBinding.inflate(layoutInflater)
        fabOpenAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.open_fab_anim)
        fabCloseAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.close_fab_anim)
        openAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.open_anim)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getClubDetails(args.clubId)
        binding.uploadBookButton.setOnClickListener {
            if (isFabMenuOpen)
                collapseFabMenu()
            else
                expandFabMenu()
        }
        binding.showQr.setOnClickListener {
            showQR()
        }
    }

    private fun getClubDetails(clubId: String) {
        viewModel.getClubDetails(clubId).observe(
            viewLifecycleOwner,
            {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.clubDetailsProgressBar.show()
                        binding.clubDetailsProgressBar.enable()
                    }
                    Result.Status.SUCCESS -> {
                        binding.clubDetailsProgressBar.hide()
                        binding.clubDetailsProgressBar.disable()
                        if (it.data?.Status == "success") {
                            binding.clubNameHeading.text = it.data.Data.Club.ClubName
                            val clubImg = binding.clubPhotoDetailsPage
                            val imgUrl =
                                it.data.Data.Club.ClubPic.toUri().buildUpon().scheme("https")
                                    .build()
                            val circularProgressDrawable =
                                CircularProgressDrawable(requireContext())
                            circularProgressDrawable.strokeWidth = 5f
                            circularProgressDrawable.centerRadius = 30f
                            circularProgressDrawable.setColorSchemeColors(
                                Color.argb(100, 244, 121, 18)
                            )
                            circularProgressDrawable.start()
                            Glide.with(clubImg.context)
                                .load(imgUrl)
                                .apply(
                                    RequestOptions()
                                        .placeholder(circularProgressDrawable)
                                        .error(R.drawable.ic_broken_image)
                                )
                                .into(clubImg)
                            binding.clubNameHeading.show()
                            binding.clubNameHeading.enable()
                            binding.clubDetailsCard.startAnimation(openAnimation)
                            binding.clubDetailsCard.show()
                            binding.clubDetailsCard.enable()
                            binding.showQr.show()
                            binding.showQr.enable()
                        }
                    }
                    Result.Status.ERROR -> {
                        context?.longToast(it.message.toString())
                    }
                }
            }
        )
    }

    private fun showQR() {
        val qrCodeImg = viewModel.encodeAsBitmap(args.clubId)
        val qrDialog = QrCodeDialogBinding.inflate(layoutInflater)
        qrDialog.qrCode.setImageBitmap(qrCodeImg)
        MaterialAlertDialogBuilder(requireContext())
            .setView(qrDialog.root)
            .setBackground(ColorDrawable(Color.TRANSPARENT))
            .show()
    }

    private fun expandFabMenu() {
        ViewCompat.animate(binding.uploadBookButton).rotation(45.0f).withLayer()
            .setDuration(300).setInterpolator(OvershootInterpolator(10.0f)).start()
        binding.uploadPdfLayout.startAnimation(fabOpenAnimation)
        binding.uploadEpubLayout.startAnimation(fabOpenAnimation)
        binding.floatingActionUploadPdf.isClickable = true
        binding.floatingActionUploadEpub.isClickable = true
        isFabMenuOpen = true
    }

    private fun collapseFabMenu() {
        ViewCompat.animate(binding.uploadBookButton).rotation(0.0f).withLayer()
            .setDuration(300).setInterpolator(OvershootInterpolator(10.0f)).start()
        binding.uploadPdfLayout.startAnimation(fabCloseAnimation)
        binding.uploadEpubLayout.startAnimation(fabCloseAnimation)
        binding.floatingActionUploadPdf.isClickable = false
        binding.floatingActionUploadEpub.isClickable = false
        isFabMenuOpen = false
    }
}
