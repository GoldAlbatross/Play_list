package com.practicum.playlistmaker.screens.media_lib.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaLibBinding
import com.practicum.playlistmaker.screens.media_lib.router.MLRouter

class MediaLibActivity : AppCompatActivity() {

    private lateinit var tabMediator: TabLayoutMediator
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMediaLibBinding.inflate(layoutInflater) }
    private val router by lazy { MLRouter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.viewPager.adapter = MLViewPagerAdapter(supportFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }.apply { attach() }

        binding.backBtn.setNavigationOnClickListener { router.onClickedBack() }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}