package com.practicum.playlistmaker.features.storage.local_db.domain.impl

import com.practicum.playlistmaker.features.itunes_api.domain.model.Track
import com.practicum.playlistmaker.features.storage.local_db.domain.api.AlbumInteractor
import com.practicum.playlistmaker.features.storage.local_db.domain.api.AlbumRepository
import com.practicum.playlistmaker.features.storage.local_db.domain.model.Album
import kotlinx.coroutines.flow.Flow

class AlbumInteractorImpl(
    private val albumRepository: AlbumRepository,
): AlbumInteractor {
    override suspend fun createAlbum(album: Album) {
        albumRepository.createAlbum(album)
    }

    override fun getAlbumList(): Flow<List<Album>> {
        return albumRepository.getSavedAlbums()
    }

    override suspend fun addToAlbum(track: Track, album: Album) {
        albumRepository.addToAlbum(track, album)
    }
}