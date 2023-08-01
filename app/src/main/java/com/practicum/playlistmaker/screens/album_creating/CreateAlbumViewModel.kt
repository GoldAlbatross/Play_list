package com.practicum.playlistmaker.screens.album_creating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.features.storage.local_db.domain.api.AlbumInteractor
import com.practicum.playlistmaker.features.storage.local_db.domain.model.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateAlbumViewModel(
    private val interactor: AlbumInteractor,
): ViewModel() {

    private val album = Album()
    private val uiState = MutableStateFlow<ScreenState>(ScreenState.Default)
    val uiStateFlow: StateFlow<ScreenState> = uiState.asStateFlow()
    fun onNameTextChange(text: String) {
        album.name = text
        viewModelScope.launch {
            if (album.name.isNotEmpty())
                uiState.emit(ScreenState.Button(true))
            else
                uiState.emit(ScreenState.Button(false))
        }
    }

    fun onDescriptionTextChange(text: String) {
        album.description = text
    }

    fun onBackPressed() {
        viewModelScope.launch {
            if (album.uri.isNotEmpty() || album.name.isNotEmpty() || album.description.isNotEmpty()) {
                uiState.emit(ScreenState.Dialog)
            } else uiState.emit(ScreenState.GoBack)
        }
    }

    fun setUri(uri: String) {
        album.uri = uri
    }

    fun dialogIsHide() {
        viewModelScope.launch {
            uiState.emit(ScreenState.Default)
        }
    }

    fun onCreatePressed() {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.emit(ScreenState.SaveAlbum(album.name))
            interactor.createAlbum(album)
        }
    }
}