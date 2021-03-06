package com.dscvit.keats.ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dscvit.keats.R
import com.dscvit.keats.databinding.ActivityPostAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostAuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostAuthBinding
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.post_auth_nav_host_fragment) as NavHostFragment).findNavController()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openUserProfile()
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

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
                    .error(R.drawable.ic_default_photo)
            )
            .into(profilePicImg)
    }

    private fun openUserProfile() {
        binding.profilePic.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
