package com.practicum.playlistmaker.presentation.fragment.favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.Logger
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoriteBinding
import com.practicum.playlistmaker.domain.network.model.Track
import com.practicum.playlistmaker.presentation.viewmodel.FavoriteListViewModel
import com.practicum.playlistmaker.utils.className
import com.practicum.playlistmaker.utils.debounce
import com.practicum.playlistmaker.utils.viewBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteListFragment: Fragment(R.layout.fragment_favorite) {

    private val logger: Logger by inject()
    private val binding by viewBinding<FragmentFavoriteBinding>()
    private val router by lazy { FavoriteRouter(requireContext()) }
    private val viewModel by viewModel<FavoriteListViewModel>()
    private val trackAdapter by lazy { FavoriteTrackAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logger.log("$className -> onViewCreated()")
        super.onViewCreated(view, savedInstanceState)

        // Initialize recycler
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = trackAdapter
        }

        // On tap to track
        trackAdapter.action = debounce(
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            action = { track -> router.openPlayerActivity(track) }
        )

        viewModel.uiStateLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoriteUIState.Empty -> showDummy()
                is FavoriteUIState.Success -> showContent(state.data)
            }
        }
    }

    private fun showDummy() {
        binding.recycler.visibility = View.INVISIBLE
        binding.dummy.visibility = View.VISIBLE
    }

    private fun showContent(list: List<Track>) {
        binding.dummy.visibility = View.INVISIBLE
        trackAdapter.trackList.clear()
        trackAdapter.trackList.addAll(list)
        trackAdapter.notifyDataSetChanged()
        binding.recycler.visibility = View.VISIBLE
    }

    companion object { fun newInstance() = FavoriteListFragment() }
}