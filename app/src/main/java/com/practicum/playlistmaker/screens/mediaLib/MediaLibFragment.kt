package com.practicum.playlistmaker.screens.mediaLib

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMediaLibBinding
import com.practicum.playlistmaker.utils.viewBinding


class MediaLibFragment : Fragment(R.layout.fragment_media_lib) {

    private lateinit var tabMediator: TabLayoutMediator
    private val binding by viewBinding<FragmentMediaLibBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = MediaLibViewPagerAdapter(
            fragmentManager = childFragmentManager,
            lifecycle = lifecycle,
        )
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }.apply { attach() }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}