package com.practicum.playlistmaker.features.storage.local_db.domain.api

import com.practicum.playlistmaker.features.storage.local_db.domain.model.Album
import kotlinx.coroutines.flow.Flow

interface AlbumInteractor {

    suspend fun createAlbum(album: Album)
    fun getAlbumlist(): Flow<List<Album>>
}