package com.practicum.playlistmaker.presentation.fragment.playlist

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.Logger
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.domain.local_db.model.Album
import com.practicum.playlistmaker.domain.local_db.model.ScreenState
import com.practicum.playlistmaker.presentation.fragment.album.AlbumFragment.Companion.ALBUM_KEY
import com.practicum.playlistmaker.presentation.viewmodel.PlayListViewModel
import com.practicum.playlistmaker.utils.className
import com.practicum.playlistmaker.utils.viewBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment: Fragment(R.layout.fragment_playlist) {

    private val logger: Logger by inject()
    private val binding by viewBinding<FragmentPlaylistBinding>()
    private val viewModel by viewModel<PlayListViewModel>()
    private val playListAdapter by lazy { PlayListAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logger.log("$className -> onViewCreated()")
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.adapter = playListAdapter
        initListeners()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiStateFlow.collect { state ->
                logger.log("$className -> viewModel.uiStateFlow.collect { state ->${state.className} }")
                when (state) {
                    is ScreenState.Content -> { drawUI(state.albums) }
                    is ScreenState.Empty -> { drawUI(emptyList()) }
                }
            }
        }
    }

    private fun drawUI(albums: List<Album>) {
        logger.log("$className -> drawUI(albums: List<Album>)")
        if (albums.isEmpty()) {
            binding.dummy.visibility = View.VISIBLE
            binding.recycler.visibility = View.INVISIBLE
        } else {
            binding.dummy.visibility = View.INVISIBLE
            binding.recycler.visibility = View.VISIBLE
            playListAdapter.playList = albums
            playListAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        logger.log("$className -> onDestroyView()")
        super.onDestroyView()
        playListAdapter.action = null
    }

    private fun initListeners() {
        binding.refreshBtn.setOnClickListener {
            findNavController().navigate(R.id.action_media_lib_to_createFragment)
        }

        // On tap to album
        playListAdapter.action = { id ->
            logger.log("$className -> playListAdapter.action = { id -> $id }")
            findNavController().navigate(
                resId = R.id.action_media_lib_to_albumFragment,
                args = bundleOf(ALBUM_KEY to id),
            )
        }
    }

    companion object { fun newInstance() = PlayListFragment() }
}