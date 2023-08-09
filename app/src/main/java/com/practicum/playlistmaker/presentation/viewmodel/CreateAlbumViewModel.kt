package com.practicum.playlistmaker.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.app.TAG
import com.practicum.playlistmaker.domain.local_db.api.PlayListInteractor
import com.practicum.playlistmaker.domain.local_db.model.Album
import com.practicum.playlistmaker.presentation.fragment.create.ScreenState
import com.practicum.playlistmaker.utils.className
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class CreateAlbumViewModel(
    private val interactor: PlayListInteractor,
): ViewModel() {

    protected var album = Album()
    protected val uiStateMutable = MutableStateFlow<ScreenState>(ScreenState.Default)
    val uiState: StateFlow<ScreenState> = uiStateMutable.asStateFlow()

    fun onNameTextChange(text: String) {
        Log.d(TAG, "${className()} -> onNameTextChange(text: $text)")
        album = album.copy(name = text)
        viewModelScope.launch {
            if (album.name.isNotEmpty())
                uiStateMutable.emit(ScreenState.Button(true))
            else
                uiStateMutable.emit(ScreenState.Button(false))
        }
    }

    fun onDescriptionTextChange(text: String) {
        Log.d(TAG, "${className()} -> onDescriptionTextChange(text: $text)")
        album = album.copy(description = text)
    }

    open fun onBackPressed() {
        Log.d(TAG, "${className()} -> onBackPressed()")
        viewModelScope.launch {
            if (album.uri.isNotEmpty() || album.name.isNotEmpty() || album.description.isNotEmpty()) {
                uiStateMutable.emit(ScreenState.Dialog)
            } else uiStateMutable.emit(ScreenState.GoBack)
        }
    }

    fun setUri(uri: String) {
        Log.d(TAG, "${className()} -> setUri(uri: $uri)")
        album = album.copy(uri = uri)
    }

    fun dialogIsHide() {
        Log.d(TAG, "${className()} -> dialogIsHide()")
        viewModelScope.launch {
            uiStateMutable.emit(ScreenState.Default)
        }
    }

    open fun onCreatePressed(data: Album?) {
        Log.d(TAG, "${className()} -> onCreatePressed()")
        viewModelScope.launch(Dispatchers.IO) {
            uiStateMutable.emit(ScreenState.SaveAlbum(album.name))
            interactor.createAlbum(album)
        }
    }
}