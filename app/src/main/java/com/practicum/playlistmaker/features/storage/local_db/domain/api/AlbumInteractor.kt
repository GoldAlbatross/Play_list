package com.practicum.playlistmaker.features.storage.local_db.domain.api

import com.practicum.playlistmaker.features.itunes_api.domain.model.Track
import com.practicum.playlistmaker.features.storage.local_db.domain.model.Album
import kotlinx.coroutines.flow.Flow

interface AlbumInteractor {

    suspend fun createAlbum(album: Album)
    fun getAlbumList(): Flow<List<Album>>
    suspend fun addToAlbum(track: Track, album: Album)
}