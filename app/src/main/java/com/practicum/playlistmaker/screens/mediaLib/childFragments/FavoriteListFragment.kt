package com.practicum.playlistmaker.screens.mediaLib.childFragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoriteBinding
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.screens.mediaLib.viewModel.FavoriteListViewModel
import com.practicum.playlistmaker.screens.search.TrackAdapter
import com.practicum.playlistmaker.utils.Debouncer
import com.practicum.playlistmaker.utils.viewBinding

class FavoriteListFragment: Fragment(R.layout.fragment_favorite) {

    private val binding by viewBinding<FragmentFavoriteBinding>()
    private val viewModel by viewModels<FavoriteListViewModel>()
    private val debouncer: Debouncer by lazy {
        Debouncer(coroutineScope = viewLifecycleOwner.lifecycleScope)
    }
    private val trackAdapter by lazy { TrackAdapter(debouncer) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize recycler
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = trackAdapter
        }
    }

    companion object { fun newInstance() = FavoriteListFragment() }
}