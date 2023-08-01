package com.practicum.playlistmaker.features.storage.local_db.domain.api

import com.practicum.playlistmaker.features.storage.local_db.domain.model.Album
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    suspend fun createAlbum(album: Album)
    fun getSavedAlbums(): Flow<List<Album>>
}