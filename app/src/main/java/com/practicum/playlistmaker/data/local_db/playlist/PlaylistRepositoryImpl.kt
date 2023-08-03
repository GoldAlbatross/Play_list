package com.practicum.playlistmaker.data.local_db.playlist

import com.practicum.playlistmaker.domain.network.model.Track
import com.practicum.playlistmaker.data.local_db.LocalDatabase
import com.practicum.playlistmaker.domain.local_db.api.PlaylistRepository
import com.practicum.playlistmaker.domain.local_db.model.Album
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PlaylistRepositoryImpl(
    private val database: LocalDatabase,
    private val convertor: AlbumDbConvertor,
): PlaylistRepository {

    override suspend fun createAlbum(album: Album) {
        database.getAlbumDao().createAlbum(convertor.map(album))
    }

    override fun getSavedAlbums(): Flow<List<Album>> {
        return database.getAlbumDao()
            .getAlbums()
            .map { album -> album.map { convertor.map(it) }
        }
    }

    override suspend fun addToAlbum(track: Track, album: Album) {
        val list = album.trackList + track
        database.getAlbumDao().updateAlbumFields(
            id = album.id,
            trackList = Json.encodeToString(list),
            trackCount = list.size,
            date = System.currentTimeMillis(),
        )
    }
}