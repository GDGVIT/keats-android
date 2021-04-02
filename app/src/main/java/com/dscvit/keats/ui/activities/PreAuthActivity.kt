package com.dscvit.keats.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dscvit.keats.databinding.ActivityPreAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreAuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
