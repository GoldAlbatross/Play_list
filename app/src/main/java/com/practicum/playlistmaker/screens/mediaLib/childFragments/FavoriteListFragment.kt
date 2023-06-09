package com.practicum.playlistmaker.screens.mediaLib.childFragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.screens.mediaLib.viewModel.FavoriteListViewModel

class FavoriteListFragment: Fragment(R.layout.fragment_favorite) {

    private val viewModel by viewModels<FavoriteListViewModel>()

    companion object { fun newInstance() = FavoriteListFragment() }
}