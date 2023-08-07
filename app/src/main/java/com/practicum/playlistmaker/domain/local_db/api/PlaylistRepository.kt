package com.practicum.playlistmaker.domain.local_db.api

import com.practicum.playlistmaker.domain.network.model.Track
import com.practicum.playlistmaker.domain.local_db.model.Album
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun createAlbum(album: Album)
    fun getSavedAlbums(): Flow<List<Album>>
    suspend fun addToAlbum(track: Track, album: Album)
}