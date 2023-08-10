package com.practicum.playlistmaker.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.app.TAG
import com.practicum.playlistmaker.domain.local_db.api.PlayListInteractor
import com.practicum.playlistmaker.presentation.fragment.album.AlbumScreenState
import com.practicum.playlistmaker.utils.className
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlbumViewModel(
    private val albumId: Long,
    private val playListInteractor: PlayListInteractor,
): ViewModel() {

    private val uiStateMutable = MutableStateFlow<AlbumScreenState>(AlbumScreenState.Default)
    val uiState: StateFlow<AlbumScreenState> = uiStateMutable.asStateFlow()

    fun getAlbum() {
        Log.d(TAG, "${className()} -> getAlbum()")
        viewModelScope.launch(Dispatchers.IO) {
            val album = playListInteractor.getAlbum(albumId)
            if (album.trackCount == 0) {
                uiStateMutable.emit(AlbumScreenState.EmptyBottomSheet(album))
            } else {
                uiStateMutable.emit(AlbumScreenState.BottomSheet(album))
            }
        }
    }

    fun deleteTrack(trackId: Int) {
        Log.d(TAG, "${className()} -> deleteTrack(trackId: $trackId)")
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.removeTrack(albumId, trackId)
            getAlbum()
        }
    }

    fun onSharePressed() {
        Log.d(TAG, "${className()} -> onSharePressed()")
        viewModelScope.launch(Dispatchers.IO) {
            uiStateMutable.emit(AlbumScreenState.Default)
            val album = playListInteractor.getAlbum(albumId)
            if (album.trackCount == 0) {
                uiStateMutable.emit(AlbumScreenState.EmptyShare)
            } else {
                uiStateMutable.emit(AlbumScreenState.Share(album))
            }
        }
    }

    fun onDotsPressed() {
        Log.d(TAG, "${className()} -> onDotsPressed()")
        viewModelScope.launch(Dispatchers.IO) {
            uiStateMutable.emit(AlbumScreenState.Default)
            val album = playListInteractor.getAlbum(albumId)
            uiStateMutable.emit(AlbumScreenState.Dots(album))
        }
    }

    fun deleteAlbum() {
        Log.d(TAG, "${className()} -> deleteAlbum()")
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.removeAlbum(albumId)
        }
    }

}