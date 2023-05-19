package com.practicum.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.model.NetworkResponse
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.model.KeyboardState
import com.practicum.playlistmaker.search.ui.model.UiState
import com.practicum.playlistmaker.utils.DELAY_1500
import com.practicum.playlistmaker.utils.SingleLiveEvent

class SearchViewModel(private val searchInteractor: SearchInteractor): ViewModel() {

    private val trackList = mutableListOf<Track>()
    private val handler = Handler(Looper.getMainLooper())
    private var latestText: String? = null

    private val uiState = MutableLiveData<UiState>()
    private val pushedItemState = SingleLiveEvent<Int>()
    private val keyboardState = SingleLiveEvent<KeyboardState>()
    init {
        trackList.addAll(searchInteractor.getTracksFromLocalStorage(HISTORY_KEY))
    }
    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }
    fun uiStateLiveData(): LiveData<UiState> = uiState
    fun pushedItemStateLiveData(): LiveData<Int> = pushedItemState
    fun keyboardStateLiveData(): LiveData<KeyboardState> = keyboardState

    fun onClickFooter() {
        uiState.postValue(UiState.Default)
        searchInteractor.clearTrackList(HISTORY_KEY)
    }
    fun onClickTrack(track: Track, position: Int) {
        //pushedItemState.postValue(position)
        searchInteractor.saveTrack(HISTORY_KEY, trackList, track)
    }
    fun onSwipeRight(track: Track, position: Int) {
        onClickTrack(track = track,position = position)
    }
    fun onSwipeLeft(track: Track) {
        searchInteractor.removeTrackFromLocalStorage(HISTORY_KEY, track = track)
    }
    fun onClickClearInput() {
        keyboardState.value = KeyboardState.HIDE
        showHistoryContent()
    }

    fun onClickInput() {
        keyboardState.value = KeyboardState.SHOW
        showHistoryContent()
    }

    fun onClickedRefresh(text: String) {
        uiState.value = UiState.Loading
        requestDataFromApi(query = text)
    }

    fun startSearchRequest(text: String) {
        searchDebounce(text = text)
    }

    fun showHistoryContent() {
        uiState.value = UiState.Loading
        uiState.value = UiState.HistoryContent(searchInteractor.getTracksFromLocalStorage(HISTORY_KEY))
    }

    private fun requestDataFromApi(query: String) {
        uiState.value = UiState.Loading
        searchInteractor.getTracksFromApi(query) { networkResponse ->
            when (networkResponse) {
                is NetworkResponse.Error -> uiState.postValue(UiState.Error)
                is NetworkResponse.Offline -> uiState.postValue(UiState.Error)
                is NetworkResponse.Success -> {
                    if (networkResponse.data!!.isEmpty())
                        uiState.postValue(UiState.SearchContent(emptyList()))
                    else
                        uiState.postValue(UiState.SearchContent(networkResponse.data))
                }
            }
        }
    }

    private fun searchDebounce(text: String) {
        if (latestText == text) return

        latestText = text
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val runnable = Runnable { requestDataFromApi(query = text) }
        val postTime = SystemClock.uptimeMillis() + DELAY_1500
        handler.postAtTime(runnable, SEARCH_REQUEST_TOKEN, postTime)
    }

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
        private const val HISTORY_KEY = "history_tracks"
        fun factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(searchInteractor = Creator.provideSearchInteractor())
                }
            }
    }
}