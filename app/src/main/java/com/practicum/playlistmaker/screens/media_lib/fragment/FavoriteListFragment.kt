package com.practicum.playlistmaker.screens.media_lib.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.screens.media_lib.view_model.FLViewModel

class FavoriteListFragment: Fragment(R.layout.fragment_favorite) {

    private val viewModel by viewModels<FLViewModel>()

    companion object { fun new() = FavoriteListFragment() }
}