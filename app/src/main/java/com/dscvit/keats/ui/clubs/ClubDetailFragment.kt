package com.dscvit.keats.ui.clubs

import android.content.Intent
import android.content.SharedPreferences
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
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dscvit.keats.BuildConfig
import com.dscvit.keats.R
import com.dscvit.keats.adapter.MemberListAdapter
import com.dscvit.keats.databinding.FragmentClubDetailBinding
import com.dscvit.keats.databinding.LeaveClubConfirmationDialogBinding
import com.dscvit.keats.databinding.MemberDetailDialogBinding
import com.dscvit.keats.databinding.QrCodeDialogBinding
import com.dscvit.keats.model.Result
import com.dscvit.keats.model.clubs.GetClubDetailsData
import com.dscvit.keats.model.clubs.KickMemberRequest
import com.dscvit.keats.model.clubs.LeaveClubRequest
import com.dscvit.keats.ui.activities.ReadingActivity
import com.dscvit.keats.utils.Constants
import com.dscvit.keats.utils.PreferenceHelper
import com.dscvit.keats.utils.disable
import com.dscvit.keats.utils.enable
import com.dscvit.keats.utils.hide
import com.dscvit.keats.utils.longToast
import com.dscvit.keats.utils.shortToast
import com.dscvit.keats.utils.show
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@AndroidEntryPoint
class ClubDetailFragment : Fragment(), MemberListAdapter.OnMemberListener {

    private lateinit var binding: FragmentClubDetailBinding
    private val viewModel: ClubDetailViewModel by viewModels()
    private val args: ClubDetailFragmentArgs by navArgs()
    private lateinit var openAnimation: Animation
    private lateinit var qrCode: Bitmap
    private lateinit var adapter: MemberListAdapter
    private var isHost = false
    private var hostId = ""
    private var clubId = ""
    private var clubName = ""
    private var clubPic = ""
    private var clubPrivate = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClubDetailBinding.inflate(layoutInflater)
        adapter = MemberListAdapter(requireContext(), this)
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
        binding.showQr.setOnClickListener {
            showQR()
        }
        binding.shareQr.setOnClickListener {
            shareQR()
        }
        binding.memberListRefresh.setColorSchemeColors(
            ResourcesCompat.getColor(resources, R.color.orange_200, null)
        )
        binding.memberListRefresh.setOnRefreshListener {
            refreshMembers()
            binding.memberListRefresh.isRefreshing = false
        }
        binding.clubDetailsCard.setOnClickListener {
            startReading()
        }
        binding.leaveClubButton.setOnClickListener {
            leaveClub()
        }
        binding.editClubButton.setOnClickListener {
            editClub()
        }
    }

    private fun refreshMembers() {
        viewModel.getClubDetails(clubId).observe(
            viewLifecycleOwner,
            {
                when (it.status) {
                    Result.Status.LOADING -> {
                    }
                    Result.Status.SUCCESS -> {
                        binding.clubDetailsProgressBar.hide()
                        binding.clubDetailsProgressBar.disable()
                        if (it.data?.Status == "success") {
                            adapter.submitList(it.data.Data.Users)
                        }
                    }
                    Result.Status.ERROR -> {
                        context?.longToast(it.message.toString())
                    }
                }
            }
        )
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

    private fun showClubDetails(data: GetClubDetailsData) {
        val sharedPref: SharedPreferences =
            PreferenceHelper.customPrefs(requireContext(), Constants.PREF_NAME)
        if (sharedPref.getString(Constants.PREF_USER_ID, "") == data.Club.HostId) {
            isHost = true
        }
        hostId = data.Club.HostId
        clubId = data.Club.ClubId
        clubName = data.Club.ClubName
        clubPrivate = data.Club.Private
        clubPic = data.Club.ClubPic
        binding.membersListHeading.show()
        binding.membersListHeading.enable()
        adapter.submitList(data.Users)
        binding.clubNameDetailsPage.text = data.Club.ClubName
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
        binding.membersList.startAnimation(openAnimation)
        binding.membersList.show()
        binding.membersList.enable()
        binding.memberListRefresh.startAnimation(openAnimation)
        binding.memberListRefresh.show()
        binding.memberListRefresh.enable()
        binding.clubNameHeading.show()
        binding.clubNameHeading.enable()
        binding.clubDetailsCard.startAnimation(openAnimation)
        binding.clubDetailsCard.show()
        binding.clubDetailsCard.enable()
        binding.showQr.show()
        binding.showQr.enable()
        binding.shareQr.show()
        binding.shareQr.enable()
        if (isHost) {
            binding.editClubButton.show()
            binding.editClubButton.enable()
        }
    }

    override fun onMemberClick(position: Int) {
        val details = adapter.getMemberDetails(position)
        val memberDetails = MemberDetailDialogBinding.inflate(layoutInflater)
        memberDetails.memberNameDialog.text = details.UserName
        memberDetails.memberBioDialog.text = details.UserBio
        memberDetails.memberEmailDialog.text = details.Email
        memberDetails.memberPhoneDialog.text = details.PhoneNumber
        val circularProgressDrawable = CircularProgressDrawable(requireContext())
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.setColorSchemeColors(
            Color.argb(100, 244, 121, 18)
        )
        circularProgressDrawable.start()
        val profileImgDialog = memberDetails.memberProfilePhotoImgDialog
        val profileImgUri = details.ProfilePic.toUri().buildUpon().scheme("https").build()
        Glide.with(profileImgDialog.context)
            .load(profileImgUri)
            .apply(
                RequestOptions()
                    .placeholder(circularProgressDrawable)
                    .error(R.drawable.ic_default_photo)
            )
            .into(profileImgDialog)
        if (isHost && details.UserId != hostId) {
            val params = memberDetails.kickMember.layoutParams
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            memberDetails.kickMember.show()
            memberDetails.kickMember.enable()
        }
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(memberDetails.root)
            .setBackground(ColorDrawable(Color.TRANSPARENT))
            .show()
        memberDetails.kickMember.setOnClickListener {
            kickMember(details.UserId, dialog)
        }
    }

    private fun kickMember(userId: String, dialog: AlertDialog) {
        val kickMemberRequest = KickMemberRequest(
            ClubId = clubId,
            UserId = userId
        )
        viewModel.kickMember(kickMemberRequest).observe(
            viewLifecycleOwner,
            {
                when (it.status) {
                    Result.Status.LOADING -> {
                    }
                    Result.Status.SUCCESS -> {
                        dialog.dismiss()
                        refreshMembers()
                        context?.shortToast("Member Removed Successfully")
                    }
                    Result.Status.ERROR -> {
                        dialog.dismiss()
                        context?.shortToast("There was error in removing the member")
                    }
                }
            }
        )
    }

    private fun startReading() {
        val intent = Intent(activity, ReadingActivity::class.java)
        intent.putExtra("ClubID", clubId)
        startActivity(intent)
    }

    private fun leaveClub() {
        val confirmDialog = LeaveClubConfirmationDialogBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(confirmDialog.root)
            .setBackground(ColorDrawable(Color.TRANSPARENT))
            .show()
        confirmDialog.confirmLeaveButton.setOnClickListener {
            if (confirmDialog.confirmType.text.toString() == "KEATS") {
                val leaveClubRequest = LeaveClubRequest(
                    ClubId = clubId
                )
                viewModel.leaveClub(leaveClubRequest).observe(
                    viewLifecycleOwner,
                    {
                        when (it.status) {
                            Result.Status.LOADING -> {
                            }
                            Result.Status.SUCCESS -> {
                                dialog.dismiss()
                                context?.shortToast("Club Left Successfully")
                                findNavController().navigate(ClubDetailFragmentDirections.actionClubDetailFragmentToClubsListFragment())
                            }
                            Result.Status.ERROR -> {
                                context?.shortToast("There was error in leaving the club")
                            }
                        }
                    }
                )
            } else {
                context?.shortToast("Enter KEATS to leave the club")
            }
        }
    }

    private fun editClub() {
        val action = ClubDetailFragmentDirections.actionClubDetailFragmentToEditClubFragment(
            clubId = clubId,
            clubName = clubName,
            clubPicUrl = clubPic,
            clubPrivate = clubPrivate
        )
        findNavController().navigate(action)
    }
}
