package com.practicum.playlistmaker.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.app.TAG
import com.practicum.playlistmaker.domain.local_db.api.PlayListInteractor
import com.practicum.playlistmaker.presentation.fragment.album.AlbumScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AlbumViewModel(
    private val albumId: Long,
    private val playListInteractor: PlayListInteractor,
): ViewModel() {

    init { getAlbum() }
    private val uiStateMutable = MutableSharedFlow<AlbumScreenState>()
    val uiState: SharedFlow<AlbumScreenState> = uiStateMutable.asSharedFlow()

    private fun getAlbum() {
        Log.d(TAG, "AlbumViewModel -> getAlbum()")
        viewModelScope.launch(Dispatchers.IO) {
            val album = playListInteractor.getAlbum(albumId)
            if (album.trackCount == 0) {
                uiStateMutable.emit(AlbumScreenState.Content(album))
            } else {
                uiStateMutable.emit(AlbumScreenState.BottomSheet(album))
            }
        }
    }

    fun deleteTrack(trackId: Int) {
        Log.d(TAG, "AlbumViewModel -> deleteTrack(trackId: $trackId)")
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.removeTrack(albumId, trackId)
            getAlbum()
        }
    }

    fun onSharePressed() {
        Log.d(TAG, "AlbumViewModel -> onSharePressed()")
        viewModelScope.launch(Dispatchers.IO) {
            val album = playListInteractor.getAlbum(albumId)
            if (album.trackCount == 0) {
                uiStateMutable.emit(AlbumScreenState.EmptyShare)
            } else {
                uiStateMutable.emit(AlbumScreenState.Share(album))
            }
        }
    }

    fun onDotsPressed() {
        Log.d(TAG, "AlbumViewModel -> onDotsPressed()")
        viewModelScope.launch(Dispatchers.IO) {
            val album = playListInteractor.getAlbum(albumId)
            uiStateMutable.emit(AlbumScreenState.Dots(album))
        }
    }

    fun deleteAlbum() {
        Log.d(TAG, "AlbumViewModel -> deleteAlbum()")
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.removeAlbum(albumId)
        }
    }

}