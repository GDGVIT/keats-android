package com.dscvit.keats.ui.activities

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dscvit.keats.databinding.ActivitySplashScreenBinding
import com.dscvit.keats.utils.Constants
import com.dscvit.keats.utils.Constants.PERMISSION_REQUEST
import com.dscvit.keats.utils.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(permissions)) {
                startApp()
            } else {
                requestPermissions(permissions, PERMISSION_REQUEST)
            }
        }
    }

    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED)
                allSuccess = false
        }
        return allSuccess
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    val requestAgain =
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(
                            permissions[i]
                        )
                    if (requestAgain) {
                        Toast.makeText(
                            this,
                            "Permission is required to use this app",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Go to settings and enable the permission",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    startApp()
                }
            }
        }
    }

    private fun startApp() {
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
