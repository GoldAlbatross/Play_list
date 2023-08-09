package com.practicum.playlistmaker.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.app.TAG
import com.practicum.playlistmaker.domain.local_db.api.PlayListInteractor
import com.practicum.playlistmaker.domain.local_db.model.Album
import com.practicum.playlistmaker.presentation.fragment.create.ScreenState
import com.practicum.playlistmaker.utils.className
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditViewModel(
    private val interactor: PlayListInteractor,
): CreateAlbumViewModel(interactor) {

    override fun onCreatePressed(data: Album?) {
        Log.d(TAG, "${className()} -> onCreatePressed()")
        viewModelScope.launch(Dispatchers.IO) {
            if (data!!.name.isNotEmpty()) {
                interactor.updateAlbum(
                    id = data.id,
                    uri = if (album.uri == "") data.uri else album.uri,
                    name = album.name,
                    description = album.description
                )
                uiStateMutable.emit(ScreenState.SaveAlbum(album.name))
            }
        }
    }

    override fun onBackPressed() {
        Log.d(TAG, "${className()} -> onBackPressed()")
        viewModelScope.launch {
            if (album.name.isNotEmpty()) {
                uiStateMutable.emit(ScreenState.GoBack)
            }
        }
    }
}