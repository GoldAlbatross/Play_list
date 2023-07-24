package com.practicum.playlistmaker.screens.mediaLib.childFragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputLayout
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.screens.mediaLib.viewModel.PlayListViewModel

class PlayListFragment: Fragment(R.layout.play_list) {

    private val viewModel by viewModels<PlayListViewModel>()
    companion object { fun newInstance() = PlayListFragment() }
}