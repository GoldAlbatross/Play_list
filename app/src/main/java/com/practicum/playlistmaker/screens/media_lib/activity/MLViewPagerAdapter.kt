package com.practicum.playlistmaker.screens.media_lib.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.screens.media_lib.fragment.FavoriteListFragment
import com.practicum.playlistmaker.screens.media_lib.fragment.PlayListFragment

class MLViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
): FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteListFragment.new()
            else -> PlayListFragment.new()
        }
    }
}