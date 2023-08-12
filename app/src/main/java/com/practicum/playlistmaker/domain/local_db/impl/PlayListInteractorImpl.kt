package com.practicum.playlistmaker.domain.local_db.impl

import android.util.Log
import com.practicum.playlistmaker.app.TAG
import com.practicum.playlistmaker.domain.network.model.Track
import com.practicum.playlistmaker.domain.local_db.api.PlayListInteractor
import com.practicum.playlistmaker.domain.local_db.api.PlaylistRepository
import com.practicum.playlistmaker.domain.local_db.model.Album
import com.practicum.playlistmaker.utils.className
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(
    private val playlistRepository: PlaylistRepository,
): PlayListInteractor {
    override suspend fun createAlbum(album: Album) {
        Log.d(TAG, "${className()} -> createAlbum(album: $album)")
        playlistRepository.createAlbum(album)
    }

    override fun getAlbumList(): Flow<List<Album>> {
        Log.d(TAG, "${className()} -> getAlbumList(): Flow<List<Album>>")
        return playlistRepository.getSavedAlbums()
    }

    override suspend fun addToAlbum(track: Track, album: Album) {
        Log.d(TAG, "${className()} -> addToAlbum(track: ${track.trackId}, album: ${album.id})")
        playlistRepository.refreshAlbum(album.copy(trackList = listOf(track) + album.trackList))
    }

    override suspend fun updateAlbum(id: Long, uri: String, name: String, description: String) {
        Log.d(TAG, "${className()} -> updateAlbum(id, uri, name, description)")
        playlistRepository.updateAlbum(id, uri, name, description)
    }

    override suspend fun getAlbum(id: Long): Album {
        Log.d(TAG, "${className()} -> getAlbum(id: $id): Album")
        return playlistRepository.getAlbum(id)
    }

    override suspend fun removeTrack(albumId: Long, trackId: Int) {
        Log.d(TAG, "${className()} -> removeTrack(albumId: $albumId, trackId: $trackId)")
        var album = playlistRepository.getAlbum(albumId)
        album = album.copy(trackList = album.trackList.filter { it.trackId != trackId })
        playlistRepository.refreshAlbum(album)
    }

    override suspend fun removeAlbum(albumId: Long) {
        Log.d(TAG, "${className()} -> removeAlbum(albumId: $albumId)")
        playlistRepository.removeAlbum(albumId)
    }

}