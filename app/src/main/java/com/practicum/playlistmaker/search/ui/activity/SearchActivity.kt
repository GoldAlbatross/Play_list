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
import com.practicum.playlistmaker.search.ui.model.KeyboardState
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

        // Send the new search request
        binding.refreshBtn.setOnClickListener {
            viewModel.onClickedRefresh(binding.input.text.toString())
        }

        // Tap to track
        trackAdapter.listener = { track, position ->
            viewModel.onClickTrack(track, position)
            handler.postDelayed({router.openPlayerActivity(track)}, DELAY_800)
        }

        // Catch the focus
        binding.input.setOnFocusChangeListener { _,hasFocus ->
            if (hasFocus) viewModel.onClickInput()
        }

        // Clear the search field and recycler
        binding.clear.setOnClickListener {
            viewModel.onClickClearInput()
        }

        // Hide the cross button
        binding.input.doOnTextChanged { text,_,_,_ ->
            if (text!!.length > MIN_LENGTH) {
                viewModel.startSearchRequest("$text")
            } else viewModel.showHistoryContent()
        }

        // Handle the UI state
        viewModel.uiStateLiveData().observe(this) { state ->
            when (state) {
                is UiState.Default -> showEmptyList()
                is UiState.Loading -> showLoadingState()
                is UiState.HistoryContent -> showHistoryTracks(list = state.list)
                is UiState.SearchContent -> showNewTracks(list = state.list)
                is UiState.Offline -> showNoDataState()
                is UiState.Error -> showErrorState()
            }
        }

        // Handle the keyboard show state
        viewModel.keyboardStateLiveData().observe(this) { state ->
            when (state!!) {
                KeyboardState.SHOW -> {
                    //btnVisibility(visibility = true)
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(binding.input, 0)
                }
                KeyboardState.HIDE -> {
                    btnVisibility(visibility = false)
                    binding.input.setText("")
                    binding.input.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(binding.input.windowToken, 0)
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

    private fun btnVisibility(visibility: Boolean) {
        binding.clear.visibility =
            if (binding.input.text.length > MIN_LENGTH && visibility)
                VISIBLE
            else INVISIBLE
    }

    private fun showLoadingState() {
        btnVisibility(visibility = true)
        binding.progressBar.visibility = VISIBLE
        showEmptyList()
    }

    private fun showEmptyList() {
        binding.recycler.visibility = INVISIBLE
        binding.header.visibility = GONE
        binding.footer.visibility = GONE
    }

    private fun showHistoryTracks(list: List<Track>) {
        trackAdapter.trackList.clear()
        trackAdapter.trackList.addAll(list)
        trackAdapter.notifyDataSetChanged()
        binding.recycler.visibility = VISIBLE
        binding.header.visibility = VISIBLE
        binding.footer.visibility = VISIBLE

    }

    private fun showNewTracks(list: List<Track>) {
        btnVisibility(visibility = true)
        binding.progressBar.visibility = GONE
        binding.recycler.visibility = VISIBLE
        setItems(list)
        binding.dummy.visibility = GONE
    }

    private fun showNoDataState() {
        btnVisibility(visibility = true)
        binding.progressBar.visibility = GONE
        binding.recycler.visibility = VISIBLE
        trackAdapter.trackList.clear()
        binding.dummy.visibility = VISIBLE
        binding.imgDummy.background = setImage(R.drawable.search_dummy_error)
        binding.txtDummy.text = getString(R.string.error)
        binding.refreshBtn.visibility = VISIBLE
    }

    private fun showErrorState() {
        btnVisibility(visibility = true)
        binding.progressBar.visibility = GONE
        binding.recycler.visibility = VISIBLE
        trackAdapter.trackList.clear()
        binding.dummy.visibility = VISIBLE
        binding.imgDummy.background = setImage(R.drawable.search_dummy_empty)
        binding.txtDummy.text = getString(R.string.empty_list)
        binding.refreshBtn.visibility = INVISIBLE
    }

    private fun setImage(image: Int) =
        AppCompatResources.getDrawable(this, image)

    private fun setItems(items: List<Track>) {
        trackAdapter.trackList.clear()
        trackAdapter.trackList.addAll(items)
        trackAdapter.notifyDataSetChanged()
    }

    private companion object { const val MIN_LENGTH = 1 }
}