package com.eyeque.eyequeconnect.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eyeque.eyequeconnect.R
import com.eyeque.eyequeconnect.databinding.ActivityBaseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}