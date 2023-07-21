package com.practicum.playlistmaker.screens.mediaLib.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.features.itunes_api.domain.model.Track
import com.practicum.playlistmaker.features.storage.db_favorite.domain.api.FavoriteInteractor
import com.practicum.playlistmaker.features.storage.db_favorite.domain.impl.FavoriteInteractorImpl
import com.practicum.playlistmaker.screens.mediaLib.model.FavoriteUIState
import com.practicum.playlistmaker.screens.search.model.UiState
import com.practicum.playlistmaker.screens.search.viewModel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class FavoriteListViewModel(
    private val favoriteInteractor: FavoriteInteractor,
): ViewModel() {
    init {
        //getTracks()
    }

    private val uiState = MutableLiveData<FavoriteUIState>()
    fun uiStateLiveData(): LiveData<FavoriteUIState> = uiState
    private fun getTracks() {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteInteractor.getFavoriteTracks().collect { list ->
                if (list.isEmpty()) uiState.postValue(FavoriteUIState.Empty)
                else uiState.postValue(FavoriteUIState.Success(list))
            }
        }
    }

    fun updateTrackList() {
        getTracks()
    }

}