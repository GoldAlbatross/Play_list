package com.practicum.playlistmaker.screens.search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.features.itunes_api.domain.model.Track
import com.practicum.playlistmaker.screens.search.model.ClearButtonState
import com.practicum.playlistmaker.screens.search.model.UiState
import com.practicum.playlistmaker.screens.search.router.SearchRouter
import com.practicum.playlistmaker.screens.search.viewModel.SearchViewModel
import com.practicum.playlistmaker.utils.Debouncer
import com.practicum.playlistmaker.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment: Fragment(R.layout.fragment_search) {

    private val binding by viewBinding<FragmentSearchBinding>()
    private val router by lazy { SearchRouter(requireContext()) }
    private val viewModel by viewModel<SearchViewModel>()
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

        // Handling a swipe or drag for RecyclerView
        val swipeHandler = SwipeHandlerCallback(requireContext(), router, viewModel, trackAdapter)
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recycler)

        binding.footer.setOnClickListener { viewModel.onClickFooter() }

        // Catch the focus
        binding.input.setOnFocusChangeListener { _,hasFocus ->
            if (hasFocus) viewModel.onCatchFocus("${binding.input.text}")
        }

        // Send the new search request
        binding.refreshBtn.setOnClickListener {
            viewModel.onClickedRefresh("${binding.input.text}")
        }

        // On tap to track
        trackAdapter.action = { track ->
            viewModel.onClickTrack(track)
            router.openPlayerActivity(track)
        }

        // Clear the search field and recycler
        binding.clear.setOnClickListener {
            viewModel.onClickClearInput()
        }

        // Hide the cross button
        binding.input.doOnTextChanged { text,_,_,_ ->
            viewModel.onTextChange("$text")
        }

        // Handle the UI state
        viewModel.uiStateLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Default -> showEmptyList()
                is UiState.Loading -> showLoadingState()
                is UiState.HistoryContent -> {
                    if (state.list.isEmpty()) showEmptyList()
                    else showHistoryTracks(list = state.list)
                }
                is UiState.SearchContent -> showNewTracks(list = state.list)
                is UiState.NoData -> showNoDataState(message = state.message)
                is UiState.Error -> showErrorState(message = state.message)
            }
        }

        // Handle the keyboard show state
        viewModel.keyboardAndClearBtnStateLiveData().observe(viewLifecycleOwner) { state ->
            when (state!!) {
                ClearButtonState.FOCUS -> showKeyboard(true)
                ClearButtonState.TEXT -> clearButtonVisibility(true)
                ClearButtonState.DEFAULT -> {
                    showKeyboard(false)
                    clearButtonVisibility(false)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.updateHistoryList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        trackAdapter.action = null
    }

    private fun clearButtonVisibility(show: Boolean) {
        binding.clear.visibility =
            if (show) View.VISIBLE
            else {
                clearInput()
                View.INVISIBLE
            }
    }

    private fun clearInput() {
        binding.input.setText("")
    }

    private fun showEmptyList() {
        binding.progressBar.isVisible = false
        binding.dummy.isVisible = false
        binding.recycler.visibility = View.INVISIBLE
        binding.header.isVisible = false
        binding.footer.isVisible = false
        trackAdapter.trackList.clear()
    }

    private fun showHistoryTracks(list: List<Track>) {
        trackAdapter.trackList.clear()
        binding.progressBar.isVisible = false
        trackAdapter.trackList.addAll(list)
        trackAdapter.notifyDataSetChanged()
        binding.recycler.isVisible = true
        binding.header.isVisible = true
        binding.footer.isVisible = true

    }

    private fun showNewTracks(list: List<Track>) {
        binding.progressBar.isVisible = false
        binding.recycler.isVisible = true
        setItems(list)
    }

    private fun showErrorState(message: String) {
        binding.progressBar.isVisible = false
        binding.dummy.isVisible = true
        binding.imgDummy.background = setImage(R.drawable.search_dummy_error)
        binding.txtDummy.text = message
        binding.refreshBtn.isVisible = true
    }

    private fun showLoadingState() {
        showEmptyList()
        binding.progressBar.isVisible = true
    }


    private fun showNoDataState(message: String) {
        binding.progressBar.isVisible = false
        binding.recycler.isVisible = true
        trackAdapter.trackList.clear()
        binding.dummy.isVisible = true
        binding.imgDummy.background = setImage(R.drawable.search_dummy_empty)
        binding.txtDummy.text = message
        binding.refreshBtn.visibility = View.INVISIBLE
    }

    private fun showKeyboard(show: Boolean) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (show)
            imm.showSoftInput(binding.input, 0)
        else
            imm.hideSoftInputFromWindow(binding.input.windowToken, 0)
    }

    private fun setImage(image: Int) =
        AppCompatResources.getDrawable(requireContext(), image)

    private fun setItems(items: List<Track>) {
        trackAdapter.trackList.clear()
        trackAdapter.trackList.addAll(items)
        trackAdapter.notifyDataSetChanged()
    }
}