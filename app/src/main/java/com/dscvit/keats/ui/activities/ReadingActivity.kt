package com.dscvit.keats.ui.activities

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dscvit.keats.databinding.ActivityReadingBinding
import com.dscvit.keats.utils.Constants
import com.dscvit.keats.utils.PreferenceHelper

class ReadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadingBinding
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = PreferenceHelper.customPrefs(this, Constants.PREF_NAME)
        val jwt = sharedPreferences.getString(Constants.PREF_AUTH_KEY, "")
        val userId = sharedPreferences.getString(Constants.PREF_USER_ID, "")
        val clubId = intent.getStringExtra("ClubID")
        val readUrl = "${Constants.WEB_VIEW_BASE_URL}club/$clubId/read?token=$jwt&userId=$userId"
        val webView = binding.readingView
        val webViewSettings = webView.settings
        webViewSettings.javaScriptEnabled = true
        webViewSettings.javaScriptCanOpenWindowsAutomatically = true
        webViewSettings.domStorageEnabled = true
        webViewSettings.builtInZoomControls = true
        webView.loadUrl(readUrl)
    }
}
