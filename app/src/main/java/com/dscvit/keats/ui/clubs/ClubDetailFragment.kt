package com.dscvit.keats.ui.clubs

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dscvit.keats.BuildConfig
import com.dscvit.keats.R
import com.dscvit.keats.adapter.MemberListAdapter
import com.dscvit.keats.databinding.FragmentClubDetailBinding
import com.dscvit.keats.databinding.QrCodeDialogBinding
import com.dscvit.keats.model.Result
import com.dscvit.keats.model.clubs.GetClubDetailsData
import com.dscvit.keats.utils.disable
import com.dscvit.keats.utils.enable
import com.dscvit.keats.utils.hide
import com.dscvit.keats.utils.longToast
import com.dscvit.keats.utils.show
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@AndroidEntryPoint
class ClubDetailFragment : Fragment() {

    private lateinit var binding: FragmentClubDetailBinding
    private val viewModel: ClubDetailViewModel by viewModels()
    private val args: ClubDetailFragmentArgs by navArgs()
    private lateinit var fabOpenAnimation: Animation
    private lateinit var fabCloseAnimation: Animation
    private lateinit var openAnimation: Animation
    private var isFabMenuOpen = false
    private lateinit var qrCode: Bitmap
    private lateinit var adapter: MemberListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClubDetailBinding.inflate(layoutInflater)
        adapter = MemberListAdapter(requireContext())
        fabOpenAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.open_fab_anim)
        fabCloseAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.close_fab_anim)
        openAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.open_anim)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.membersList.adapter = adapter
        getClubDetails(args.clubId)
        val a = viewModel.encodeAsBitmap(args.clubId)
        if (a != null) {
            qrCode = a
        }
        binding.uploadBookButton.setOnClickListener {
            if (isFabMenuOpen)
                collapseFabMenu()
            else
                expandFabMenu()
        }
        binding.showQr.setOnClickListener {
            showQR()
        }
        binding.shareQr.setOnClickListener {
            shareQR()
        }
    }

    private fun shareQR() {
        val filesDir: File? = context?.applicationContext?.filesDir
        val imageFile = File(filesDir, "${args.clubId}.png")
        val os: OutputStream
        try {
            os = FileOutputStream(imageFile)
            qrCode.compress(Bitmap.CompressFormat.PNG, 100, os)
            os.flush()
            os.close()
        } catch (e: Exception) {
            Timber.e(e, javaClass.simpleName, "Error writing bitmap")
        }
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val imageUri: Uri? =
            context?.let {
                FileProvider.getUriForFile(
                    it,
                    BuildConfig.APPLICATION_ID,
                    imageFile
                )
            }
        intent.putExtra(Intent.EXTRA_STREAM, imageUri)
        intent.type = "image/*"
        context?.startActivity(intent)
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
                            showClubDetails(it.data.Data)
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
        val qrDialog = QrCodeDialogBinding.inflate(layoutInflater)
        qrDialog.qrCode.setImageBitmap(qrCode)
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

    private fun showClubDetails(data: GetClubDetailsData) {
        binding.membersListHeading.show()
        binding.membersListHeading.enable()
        adapter.submitList(data.Users)
        binding.clubNameHeading.text = data.Club.ClubName
        binding.hostName.text = getString(R.string.host_club_display, data.Club.HostName)
        binding.noOfPeople.text = if (data.Users.size == 1) {
            getString(R.string.no_of_members_club_details, data.Users.size.toString(), "")
        } else {
            getString(R.string.no_of_members_club_details, data.Users.size.toString(), "s")
        }
        binding.clubStatus.text = if (data.Club.Private) {
            getString(R.string.club_type_private)
        } else {
            getString(R.string.club_type_public)
        }
        val clubImg = binding.clubPhotoDetailsPage
        val imgUrl =
            data.Club.ClubPic.toUri().buildUpon().scheme("https")
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
        binding.shareQr.show()
        binding.shareQr.enable()
    }
}
