package com.practicum.playlistmaker.presentation.fragment.media_library

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.Logger
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMediaLibBinding
import com.practicum.playlistmaker.utils.className
import com.practicum.playlistmaker.utils.viewBinding
import org.koin.android.ext.android.inject


class MediaLibFragment : Fragment(R.layout.fragment_media_lib) {

    private val logger: Logger by inject()
    private var tabMediator: TabLayoutMediator? = null
    private val binding by viewBinding<FragmentMediaLibBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logger.log("$className -> onViewCreated()")
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
        tabMediator?.detach()
    }
}