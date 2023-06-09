package com.practicum.playlistmaker.screens.mediaLib.childFragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.screens.mediaLib.viewModel.PlayListViewModel

class PlayListFragment: Fragment(R.layout.fragment_playlist) {

    private val viewModel by viewModels<PlayListViewModel>()

    companion object { fun newInstance() = PlayListFragment() }
}