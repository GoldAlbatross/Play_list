package com.practicum.playlistmaker.screens.media_library.childFragments.playlist_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.features.storage.local_db.domain.api.AlbumInteractor
import com.practicum.playlistmaker.features.storage.local_db.domain.model.ScreenState
import com.practicum.playlistmaker.features.storage.local_db.domain.model.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayListViewModel(
    private val albumInteractor: AlbumInteractor
): ViewModel() {

    private val uiState = MutableStateFlow<ScreenState>(ScreenState.Empty)
    val uiStateFlow: StateFlow<ScreenState> = uiState.asStateFlow()

    init { getData() }

    private fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            albumInteractor.getAlbumList().collect { list ->
                if (list.isEmpty()) uiState.emit(ScreenState.Empty)
                else uiState.emit(ScreenState.Content(list))
            }
        }

    }

    fun onClickTrack(album: Album) {
        //TODO("Not yet implemented")
    }

}