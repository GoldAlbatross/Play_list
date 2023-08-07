package com.practicum.playlistmaker.domain.local_db.impl

import com.practicum.playlistmaker.domain.network.model.Track
import com.practicum.playlistmaker.domain.local_db.api.PlayListInteractor
import com.practicum.playlistmaker.domain.local_db.api.PlaylistRepository
import com.practicum.playlistmaker.domain.local_db.model.Album
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(
    private val playlistRepository: PlaylistRepository,
): PlayListInteractor {
    override suspend fun createAlbum(album: Album) {
        playlistRepository.createAlbum(album)
    }

    override fun getAlbumList(): Flow<List<Album>> {
        return playlistRepository.getSavedAlbums()
    }

    override suspend fun addToAlbum(track: Track, album: Album) {
        playlistRepository.addToAlbum(track, album)
    }
}