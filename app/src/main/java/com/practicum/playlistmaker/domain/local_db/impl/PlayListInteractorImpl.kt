package com.practicum.playlistmaker.domain.local_db.impl

import com.practicum.playlistmaker.Logger
import com.practicum.playlistmaker.domain.local_db.api.PlayListInteractor
import com.practicum.playlistmaker.domain.local_db.api.PlaylistRepository
import com.practicum.playlistmaker.domain.local_db.model.Album
import com.practicum.playlistmaker.domain.network.model.Track
import com.practicum.playlistmaker.utils.className
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(
    private val playlistRepository: PlaylistRepository,
    private val logger: Logger
): PlayListInteractor {
    override suspend fun createAlbum(album: Album) {
        logger.log("$className -> createAlbum(album: $album)")
        playlistRepository.createAlbum(album)
    }

    override fun getAlbumList(): Flow<List<Album>> {
        logger.log("$className -> getAlbumList(): Flow<List<Album>>")
        return playlistRepository.getSavedAlbums()
    }

    override suspend fun addToAlbum(track: Track, album: Album) {
        logger.log("$className -> addToAlbum(track: ${track.trackId}, album: ${album.id})")
        playlistRepository.refreshAlbum(album.copy(trackList = listOf(track) + album.trackList))
    }

    override suspend fun updateAlbum(id: Long, uri: String, name: String, description: String) {
        logger.log("$className -> updateAlbum(id, uri, name, description)")
        playlistRepository.updateAlbum(id, uri, name, description)
    }

    override suspend fun getAlbum(id: Long): Album {
        logger.log("$className -> getAlbum(id: $id): Album")
        return playlistRepository.getAlbum(id)
    }

    override suspend fun removeTrack(albumId: Long, trackId: Int) {
        logger.log("$className -> removeTrack(albumId: $albumId, trackId: $trackId)")
        var album = playlistRepository.getAlbum(albumId)
        album = album.copy(trackList = album.trackList.filter { it.trackId != trackId })
        playlistRepository.refreshAlbum(album)
    }

    override suspend fun removeAlbum(albumId: Long) {
        logger.log("$className -> removeAlbum(albumId: $albumId)")
        playlistRepository.removeAlbum(albumId)
    }
}