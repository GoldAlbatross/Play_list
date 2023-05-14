package com.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.ui.model.UiState

class SearchViewModel(private val searchInteractor: SearchInteractor): ViewModel() {

    private val uiState = MutableLiveData<UiState>()
    fun uiState(): LiveData<UiState> = uiState


    fun onClickFooter() {
        searchInteractor.clearHistory()
        uiState.postValue(UiState.Default)
    }


    companion object {
        fun factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(searchInteractor = Creator.provideSearchInteractor())
                }
            }
    }
}