package com.practicum.playlistmaker.presentation.fragment.playlist_fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.domain.local_db.model.ScreenState
import com.practicum.playlistmaker.domain.local_db.model.Album
import com.practicum.playlistmaker.presentation.viewmodel.PlayListViewModel
import com.practicum.playlistmaker.utils.Debouncer
import com.practicum.playlistmaker.utils.viewBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment: Fragment(R.layout.fragment_playlist) {

    private val binding by viewBinding<FragmentPlaylistBinding>()
    private val viewModel by viewModel<PlayListViewModel>()
    private val debouncer by lazy { Debouncer(viewLifecycleOwner.lifecycleScope) }
    private val playListAdapter by lazy { PlayListAdapter(debouncer) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.adapter = playListAdapter
        initListeners()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiStateFlow.collect { state ->
                when (state) {
                    is ScreenState.Content -> { drawUI(state.albums) }
                    is ScreenState.Empty -> { drawUI(emptyList()) }
                }
            }
        }

    }

    private fun drawUI(albums: List<Album>) {
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

    private fun initListeners() {
        binding.refreshBtn.setOnClickListener {
            findNavController().navigate(R.id.action_media_lib_to_createFragment)
        }

        // On tap to album
        playListAdapter.action = { album ->
            viewModel.onClickTrack(album)
            //router.openPlayerActivity(album)
        }
    }

    companion object { fun newInstance() = PlayListFragment() }
}