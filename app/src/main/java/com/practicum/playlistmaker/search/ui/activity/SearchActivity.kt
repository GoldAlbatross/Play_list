package com.practicum.playlistmaker.search.ui.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.model.ClearButtonState
import com.practicum.playlistmaker.search.ui.model.UiState
import com.practicum.playlistmaker.search.ui.router.SearchRouter
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.utils.DELAY_800

class SearchActivity: AppCompatActivity() {

    private val router by lazy { SearchRouter(this) }
    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this,
        SearchViewModel.factory())[SearchViewModel::class.java] }
    private val handler = Handler(Looper.getMainLooper())
    private val trackAdapter = TrackAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize recycler
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = trackAdapter
        }

        // Handling a swipe or drag for RecyclerView
        val swipeHandler = SwipeHandlerCallback(this,handler, router, viewModel, trackAdapter)
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recycler)

        binding.backBtn.setNavigationOnClickListener { router.goBack() }
        binding.footer.setOnClickListener { viewModel.onClickFooter() }

        // Catch the focus
        binding.input.setOnFocusChangeListener { _,hasFocus ->
            if (hasFocus) viewModel.onClickInput("${binding.input.text}")
        }

        // Send the new search request
        binding.refreshBtn.setOnClickListener {
            viewModel.onClickedRefresh("${binding.input.text}")
        }

        // Tap to track
        trackAdapter.listener = { track, position ->
            viewModel.onClickTrack(track, position)
            handler.postDelayed({router.openPlayerActivity(track)}, DELAY_800)
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
        viewModel.uiStateLiveData().observe(this) { state ->
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
        viewModel.clearButtonStateLiveData().observe(this) { state ->
            when (state!!) {
                ClearButtonState.FOCUS -> showKeyboard(true)
                ClearButtonState.TEXT -> clearButtonVisibility(true)
                ClearButtonState.DEFAULT -> {
                    showKeyboard(false)
                    clearButtonVisibility(false)
                }
            }
        }

        // Handle the animation after a track tap
        viewModel.pushedItemStateLiveData().observe(this) { position ->
            trackAdapter.popItem(position = position)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        trackAdapter.listener = null
    }

    private fun clearButtonVisibility(show: Boolean) {
        binding.clear.visibility =
            if (show) VISIBLE
            else {
                binding.input.setText("")
                binding.input.clearFocus()
                INVISIBLE
            }
    }

    private fun showEmptyList() {
        binding.progressBar.visibility = GONE
        binding.dummy.visibility = GONE
        binding.recycler.visibility = INVISIBLE
        binding.header.visibility = GONE
        binding.footer.visibility = GONE
        trackAdapter.trackList.clear()
    }

    private fun showHistoryTracks(list: List<Track>) {
        binding.progressBar.visibility = GONE
        trackAdapter.trackList.addAll(list)
        trackAdapter.notifyDataSetChanged()
        binding.recycler.visibility = VISIBLE
        binding.header.visibility = VISIBLE
        binding.footer.visibility = VISIBLE

    }

    private fun showNewTracks(list: List<Track>) {
        binding.progressBar.visibility = GONE
        binding.recycler.visibility = VISIBLE
        setItems(list)
    }

    private fun showErrorState(message: String) {
        binding.progressBar.visibility = GONE
        binding.dummy.visibility = VISIBLE
        binding.imgDummy.background = setImage(R.drawable.search_dummy_error)
        binding.txtDummy.text = message
        binding.refreshBtn.visibility = VISIBLE
    }

    private fun showLoadingState() {
        showEmptyList()
        binding.progressBar.visibility = VISIBLE
    }


    private fun showNoDataState(message: String) {
        binding.progressBar.visibility = GONE
        binding.recycler.visibility = VISIBLE
        trackAdapter.trackList.clear()
        binding.dummy.visibility = VISIBLE
        binding.imgDummy.background = setImage(R.drawable.search_dummy_empty)
        binding.txtDummy.text = message
        binding.refreshBtn.visibility = INVISIBLE
    }

    private fun showKeyboard(show: Boolean) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (show)
            imm.showSoftInput(binding.input, 0)
        else
            imm.hideSoftInputFromWindow(binding.input.windowToken, 0)
    }

    private fun setImage(image: Int) =
        AppCompatResources.getDrawable(this, image)

    private fun setItems(items: List<Track>) {
        trackAdapter.trackList.clear()
        trackAdapter.trackList.addAll(items)
        trackAdapter.notifyDataSetChanged()
    }

    private companion object { const val QUERY_KEY = "query_key" }
}