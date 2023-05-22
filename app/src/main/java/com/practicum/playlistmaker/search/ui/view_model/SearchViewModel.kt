package com.practicum.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
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
import com.practicum.playlistmaker.search.ui.model.ClearButtonState
import com.practicum.playlistmaker.search.ui.model.UiState
import com.practicum.playlistmaker.utils.DELAY_1500
import com.practicum.playlistmaker.utils.SingleLiveEvent
import java.util.concurrent.Executors

class SearchViewModel(private val searchInteractor: SearchInteractor): ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
    private var latestText: String = ""
    private val executor = Executors.newCachedThreadPool()

    private val uiState = MutableLiveData<UiState>()
    private val keyboardAndClearBtnState = SingleLiveEvent<ClearButtonState>()

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }
    fun uiStateLiveData(): LiveData<UiState> = uiState
    fun keyboardAndClearBtnStateLiveData(): LiveData<ClearButtonState> = keyboardAndClearBtnState

    fun onClickFooter() {
        uiState.postValue(UiState.Default)
        searchInteractor.clearTrackList(HISTORY_KEY)
    }
    fun onClickTrack(track: Track) {
        executor.execute { searchInteractor.saveTrackAsFirst(HISTORY_KEY, track) }
    }
    fun onSwipeRight(track: Track) {
        onClickTrack(track = track)
    }
    fun onSwipeLeft(track: Track) {
        executor.execute { searchInteractor.removeTrackFromLocalStorage(HISTORY_KEY, track = track) }
    }
    fun onClickClearInput() {
        keyboardAndClearBtnState.value = ClearButtonState.DEFAULT
        latestText = ""
    }

    fun onCatchFocus(query: String) {
        if (query.isEmpty()) {
            showHistoryContent()
        }
    }

    fun onClickedRefresh(text: String) {
        uiState.value = UiState.Loading
        getTracksFromBackendApi(query = text)
    }

    fun onTextChange(text: String) {
        if (text.isEmpty()) {
            showHistoryContent()
        } else {
            searchDebounce(text = text)
            keyboardAndClearBtnState.value = ClearButtonState.TEXT
        }
    }

    fun onStop(text: String) {
        if (text.isEmpty()) showHistoryContent()
    }

    private fun showHistoryContent() {
        uiState.value = UiState.Loading
        executor.execute {
            uiState.postValue(
                UiState.HistoryContent(
                    searchInteractor.getTracksFromLocalStorage(HISTORY_KEY)
                )
            )
        }
    }

    private fun getTracksFromBackendApi(query: String) {
        uiState.value = UiState.Loading
        executor.execute{
            searchInteractor.getTracksFromBackendApi(query) { networkResponse ->
                when (networkResponse) {
                    is NetworkResponse.Error -> {
                        uiState.postValue(UiState.Error(networkResponse.message))
                    }
                    is NetworkResponse.Offline -> {
                        uiState.postValue(UiState.Error(networkResponse.message))
                    }
                    is NetworkResponse.Success -> {
                            uiState.postValue(UiState.SearchContent(networkResponse.data))
                    }
                    is NetworkResponse.NoData -> {
                            uiState.postValue(UiState.NoData(message = networkResponse.message))
                    }
                }
            }
        }
    }

    private fun searchDebounce(text: String) {
        if (latestText == text) return

        latestText = text
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val runnable = Runnable { getTracksFromBackendApi(query = text) }
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