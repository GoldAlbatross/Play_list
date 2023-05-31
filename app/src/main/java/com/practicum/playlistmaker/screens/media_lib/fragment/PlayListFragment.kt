package com.practicum.playlistmaker.screens.media_lib.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.screens.media_lib.view_model.PLViewModel

class PlayListFragment: Fragment(R.layout.fragment_playlist) {

    private val viewModel by viewModels<PLViewModel>()

    companion object { fun new() = PlayListFragment() }
}