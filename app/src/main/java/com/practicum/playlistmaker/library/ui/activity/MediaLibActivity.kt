package com.practicum.playlistmaker.library.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaLibBinding

class MediaLibActivity : AppCompatActivity(R.layout.activity_media_lib) {

    private val binding by lazy { ActivityMediaLibBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}