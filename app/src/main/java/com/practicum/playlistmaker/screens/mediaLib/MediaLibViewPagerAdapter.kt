package com.practicum.playlistmaker.screens.mediaLib

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.screens.mediaLib.childFragments.FavoriteListFragment
import com.practicum.playlistmaker.screens.mediaLib.childFragments.PlayListFragment

class MediaLibViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
): FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteListFragment.newInstance()
            else -> PlayListFragment.newInstance()
        }
    }
}