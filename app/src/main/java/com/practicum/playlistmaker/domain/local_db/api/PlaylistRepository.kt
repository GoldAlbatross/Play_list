package com.practicum.playlistmaker.domain.local_db.api

import com.practicum.playlistmaker.domain.local_db.model.Album
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun createAlbum(album: Album)
    fun getSavedAlbums(): Flow<List<Album>>
    suspend fun refreshAlbum(album: Album)
    suspend fun getAlbum(id: Long): Album
    suspend fun removeAlbum(albumId: Long)
    suspend fun updateAlbum(id: Long, uri: String, name: String, description: String)
}