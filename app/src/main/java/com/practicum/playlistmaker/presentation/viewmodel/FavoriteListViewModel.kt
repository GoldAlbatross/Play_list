package com.practicum.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.local_db.api.FavoriteInteractor
import com.practicum.playlistmaker.presentation.fragment.favorite_fragment.FavoriteUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteListViewModel(
    private val favoriteInteractor: FavoriteInteractor,
): ViewModel() {

    init { getTracks() }

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

}