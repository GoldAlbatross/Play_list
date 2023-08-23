package com.practicum.playlistmaker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.Logger
import com.practicum.playlistmaker.domain.local_db.api.PlayListInteractor
import com.practicum.playlistmaker.domain.local_db.model.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayListViewModel(
    private val playListInteractor: PlayListInteractor,
    private val logger: Logger
): ViewModel() {

    private val uiState = MutableStateFlow<ScreenState>(ScreenState.Empty)
    val uiStateFlow: StateFlow<ScreenState> = uiState.asStateFlow()

    init { getData() }

    private fun getData() {
        logger.log("PlayListViewModel: getData()")
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.getAlbumList().collect { list ->
                if (list.isEmpty()) uiState.emit(ScreenState.Empty)
                else uiState.emit(ScreenState.Content(list))
            }
        }
    }
}