package com.dscvit.keats.ui.activities

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.navigation.findNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dscvit.keats.R
import com.dscvit.keats.databinding.ActivityPostAuthBinding
import com.dscvit.keats.ui.clubs.ClubsListFragmentDirections
import com.dscvit.keats.utils.disable
import com.dscvit.keats.utils.enable
import com.dscvit.keats.utils.hide
import com.dscvit.keats.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostAuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostAuthBinding
    private val navController by lazy {
        this.findNavController(R.id.post_auth_nav_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openUserProfile()
    }

    fun setProfilePhoto(profileImageUrl: String) {
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.setColorSchemeColors(
            Color.argb(100, 244, 121, 18)
        )
        circularProgressDrawable.start()
        val profilePicImg = binding.profilePic
        val imgUri = profileImageUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(profilePicImg.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(circularProgressDrawable)
                    .error(R.drawable.ic_broken_image)
            )
            .into(profilePicImg)
    }

    private fun openUserProfile() {
        binding.profilePic.setOnClickListener {
            navController.navigate(ClubsListFragmentDirections.actionClubsListFragmentToUserProfileFragment())
            binding.profilePic.hide()
            binding.profilePic.disable()
        }
    }

    fun showProfilePic() {
        binding.profilePic.show()
        binding.profilePic.enable()
    }
}
