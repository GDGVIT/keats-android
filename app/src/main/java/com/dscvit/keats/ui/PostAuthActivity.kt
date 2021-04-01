package com.dscvit.keats.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dscvit.keats.R
import com.dscvit.keats.databinding.ActivityPostAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostAuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun setProfilePhoto(profileImageUrl: String) {
        val profilePicImg = binding.profilePic
        val imgUri = profileImageUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(profilePicImg.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_default_photo)
                    .error(R.drawable.ic_broken_image)
            )
            .into(profilePicImg)
    }
}
