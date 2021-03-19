package com.dscvit.keats.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
}
