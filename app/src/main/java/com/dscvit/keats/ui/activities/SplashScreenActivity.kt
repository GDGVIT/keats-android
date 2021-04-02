package com.dscvit.keats.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.dscvit.keats.databinding.ActivitySplashScreenBinding
import com.dscvit.keats.utils.Constants
import com.dscvit.keats.utils.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var intent: Intent
        sharedPreferences = PreferenceHelper.customPrefs(this, Constants.PREF_NAME)
        val isLoggedIn = sharedPreferences.getBoolean(Constants.PREF_IS_LOGGED_IN, false)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                intent = if (isLoggedIn) {
                    Intent(this, PostAuthActivity::class.java)
                } else {
                    Intent(this, PreAuthActivity::class.java)
                }
                startActivity(intent)
                finish()
            },
            Constants.SPLASH_SCREEN_DELAY
        )
    }
}
