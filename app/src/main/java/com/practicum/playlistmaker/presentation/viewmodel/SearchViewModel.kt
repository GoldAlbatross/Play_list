package com.practicum.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.network.api.SearchInteractor
import com.practicum.playlistmaker.domain.network.model.NetworkResponse
import com.practicum.playlistmaker.domain.network.model.Track
import com.practicum.playlistmaker.domain.shared_prefs.api.StorageInteractor
import com.practicum.playlistmaker.presentation.fragment.search.model.ClearButtonState
import com.practicum.playlistmaker.presentation.fragment.search.model.UiState
import com.practicum.playlistmaker.utils.DELAY_1500
import com.practicum.playlistmaker.utils.SingleLiveEvent
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val storageInteractor: StorageInteractor,
): ViewModel() {

    private var latestText: String? = null

    private val uiState = MutableLiveData<UiState>()
    private val keyboardAndClearBtnState = SingleLiveEvent<ClearButtonState>()

    private var searchJob: Job? = null

    private val deferredAction = debounce<String>(
        delayMillis = DELAY_1500,
        coroutineScope = viewModelScope,
        deferredUsing = true,
        action = { text -> getTracksFromBackendApi(query = text) }
    )

    fun uiStateLiveData(): LiveData<UiState> = uiState
    fun keyboardAndClearBtnStateLiveData(): LiveData<ClearButtonState> = keyboardAndClearBtnState

    fun onClickFooter() {
        uiState.postValue(UiState.Default)
        storageInteractor.clearTrackList(HISTORY_KEY)
    }
    fun onClickTrack(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            storageInteractor.saveTrackAsFirstToLocalStorage(HISTORY_KEY, track)
        }
    }
    fun onSwipeRight(track: Track) {
        onClickTrack(track = track)
    }
    fun onSwipeLeft(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            storageInteractor.removeTrackFromLocalStorage(HISTORY_KEY, track = track)
        }
    }
    fun onClickClearInput() {
        keyboardAndClearBtnState.value = ClearButtonState.DEFAULT
        latestText = ""
    }

    fun onCatchFocus(query: String) {
        if (query.isEmpty()) {
            latestText = ""
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

    fun updateHistoryList() {
        if (latestText == null) return
        if (latestText!!.isEmpty()) {
            showHistoryContent()
        }
    }

    private fun showHistoryContent() {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.postValue(
                UiState.HistoryContent(
                    storageInteractor.getTracksFromLocalStorage(HISTORY_KEY)
                )
            )
        }
    }

    private fun getTracksFromBackendApi(query: String) {
        uiState.value = UiState.Loading

        searchJob = viewModelScope.launch(Dispatchers.IO) {
            searchInteractor.getTracksFromBackendApi(query).collect { networkResponse ->
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
        if (latestText != text) {
            latestText = text
            deferredAction(text)
        }
    }

    companion object {
        private const val HISTORY_KEY = "history_tracks"
    }
}