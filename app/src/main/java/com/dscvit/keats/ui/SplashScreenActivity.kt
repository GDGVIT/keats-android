package com.dscvit.keats.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.dscvit.keats.databinding.ActivitySplashScreenBinding
import com.dscvit.keats.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                val intent = Intent(this, PreAuthActivity::class.java)
                startActivity(intent)
                finish()
            },
            Constants.SPLASH_SCREEN_DELAY
        )
    }
}
