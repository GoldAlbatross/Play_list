package com.practicum.playlistmaker.domain.local_db.api

import com.practicum.playlistmaker.domain.network.model.Track
import com.practicum.playlistmaker.domain.local_db.model.Album
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {

    suspend fun createAlbum(album: Album)
    fun getAlbumList(): Flow<List<Album>>
    suspend fun addToAlbum(track: Track, album: Album)
    suspend fun getAlbum(id: Long): Album
    suspend fun removeTrack(albumId: Long, trackId: Int)
    suspend fun removeAlbum(albumId: Long)
    suspend fun updateAlbum(id: Long, uri: String, name: String, description: String)
}