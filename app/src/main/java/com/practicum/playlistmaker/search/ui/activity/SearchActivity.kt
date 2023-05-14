package com.practicum.playlistmaker.search.ui.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.application.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.domain.model.NetworkResponse
import com.practicum.playlistmaker.TrackRetrofit
import com.practicum.playlistmaker.TrackRetrofitListener
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.ui.SearchRouter
import com.practicum.playlistmaker.search.ui.SwipeHandlerCallback
import com.practicum.playlistmaker.search.ui.TrackAdapter
import com.practicum.playlistmaker.search.ui.model.UiState
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.utils.DELAY_1500
import com.practicum.playlistmaker.utils.DELAY_500
import com.practicum.playlistmaker.utils.KEY_STATE
import com.practicum.playlistmaker.utils.getParcelableFromBundle
import kotlinx.parcelize.Parcelize

class SearchActivity: AppCompatActivity() {

    private val router by lazy { SearchRouter(this) }
    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this,
        SearchViewModel.factory())[SearchViewModel::class.java] }



    private val requestDataRunnable = Runnable { requestData() }

    // variables for save state of SearchActivity
    private lateinit var state: State
    private val callback = Runnable { renderState() }
    private val handler = Handler(Looper.getMainLooper())

    // variables for RecyclerView
    private val trackAdapter = TrackAdapter()

    //variables for Retrofit
    private val retrofit = TrackRetrofit()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { router.goBack() }

        viewModel.uiState().observe(this) { uiState ->
            when (uiState) {
                is UiState.Default -> { showEmptyList() }
                is UiState.Error -> TODO()
                is UiState.HistoryContent -> TODO()
                is UiState.SearchContent -> TODO()
            }
        }


        //binds View

        //handling a state
        state = savedInstanceState?.getParcelableFromBundle(KEY_STATE, State::class.java) ?: State()
        binding.request.setText(state.searchText)
        handler.postDelayed(callback, DELAY_500)
        binding.clear.visibility = state.cross

        // init the recyclerView
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = trackAdapter
        }

        // Handling a swipe or drag
        val swipeHandler = SwipeHandlerCallback(this, trackAdapter)
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recycler)

        trackAdapter.listener = { track ->
            App.instance.trackStorage.addTrack(track)
            router.openPlayerActivity(track)
        }
    }

    override fun onResume() {
        super.onResume()

        setItems(App.instance.trackStorage.getTracks())
        //trackAdapter.trackList.addAll(App.instance.trackStorage.getTracks())
        //if (recycler.adapter != null) recycler.adapter?.notifyDataSetChanged()

        // hide the cross button
        binding.request.doOnTextChanged { text, _, _, _ ->
            if (binding.request.hasFocus() && text?.isNotEmpty() == true) {
                requestDataDebounce()
                showEmptyList()
            }
            else
                showHistorySearch(binding.request.hasFocus())
            btnVisibility(text, binding.clear)
        }

        // catch the focus and set visibility
        binding.request.setOnFocusChangeListener { _, hasFocus ->
            if (binding.request.text.isNullOrEmpty()) showHistorySearch(hasFocus)
        }

        // clearing search field and recycler
        binding.clear.setOnClickListener {
            binding.request.setText("")
            binding.request.clearFocus()
            state.focus = false
            renderState()
        }


        // refresh request
        binding.btnDummy.setOnClickListener {
            retrofit.getResponseFromBackend(binding.request.text.toString())
        }

        binding.footer.setOnClickListener { viewModel.onClickFooter() }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        state.searchText = binding.request.text.toString()
        state.focus = binding.request.hasFocus()
        state.cross = binding.clear.visibility
        outState.putParcelable(KEY_STATE, state)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(callback)
        retrofit.listener = null
    }

    private fun requestData () {
        retrofit.listener = object : TrackRetrofitListener {

            override fun onSuccess(response: List<Track>) {
                if (response.isNotEmpty())
                    handlingSearchQuery(NetworkResponse.Success(response))
                else
                    handlingSearchQuery(NetworkResponse.NoData)
            }

            override fun onError(t: Throwable) =
                handlingSearchQuery(NetworkResponse.Error(t.toString()))
        }
        retrofit.getResponseFromBackend(binding.request.text.toString())
    }

    private fun renderState() {
        if (state.focus) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.request, 0)
        } else {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(binding.request.windowToken, 0)
        }
    }

    private fun btnVisibility(text: CharSequence?, view: View) {
        view.visibility = if (text.isNullOrEmpty()) INVISIBLE else VISIBLE
    }

    private fun showEmptyList() {
        binding.recycler.visibility = INVISIBLE
        binding.header.visibility = GONE
        binding.footer.visibility = GONE
    }

    private fun showHistorySearch(hasFocus: Boolean) {
        if (hasFocus && trackAdapter.trackList.isNotEmpty()) {
            binding.recycler.visibility = VISIBLE
            binding.header.visibility = VISIBLE
            binding.footer.visibility = VISIBLE
            setItems(App.instance.trackStorage.getTracks())
        } else showEmptyList()
    }

    private fun handlingSearchQuery(response: NetworkResponse) {
        binding.progressBar.visibility = GONE
        binding.recycler.visibility = VISIBLE
        when (response) {
            is NetworkResponse.Success -> {
                setItems(response.listFromApi)
                binding.dummy.visibility = GONE
            }
            is NetworkResponse.NoData -> {
                trackAdapter.trackList.clear()
                binding.dummy.visibility = VISIBLE
                binding.imgDummy.background = setImage(R.drawable.search_dummy_empty)
                binding.txtDummy.text = getString(R.string.empty_list)
                binding.btnDummy.visibility = INVISIBLE
            }
            is NetworkResponse.Error -> {
                trackAdapter.trackList.clear()
                binding.dummy.visibility = VISIBLE
                binding.imgDummy.background = setImage(R.drawable.search_dummy_error)
                binding.txtDummy.text = getString(R.string.error)
                binding.btnDummy.visibility = VISIBLE
            }
        }
    }

    private fun requestDataDebounce() {
        binding.progressBar.visibility = VISIBLE
        handler.removeCallbacks(requestDataRunnable)
        handler.postDelayed(requestDataRunnable, DELAY_1500)
    }

    private fun setImage(image: Int) =
        AppCompatResources.getDrawable(this, image)

    private fun setItems(items: List<Track>?) {
        trackAdapter.trackList.clear()
        if (items != null) trackAdapter.trackList.addAll(items)
        trackAdapter.notifyDataSetChanged()
    }

    @Parcelize
    class State(
        var searchText: String = "",
        var focus: Boolean = false,
        var cross: Int = GONE,
    ) : Parcelable
}