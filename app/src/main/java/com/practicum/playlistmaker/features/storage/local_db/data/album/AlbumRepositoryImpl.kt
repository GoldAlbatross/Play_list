package com.practicum.playlistmaker.features.storage.local_db.data.album

import com.practicum.playlistmaker.features.itunes_api.domain.model.Track
import com.practicum.playlistmaker.features.storage.local_db.data.LocalDatabase
import com.practicum.playlistmaker.features.storage.local_db.domain.api.AlbumRepository
import com.practicum.playlistmaker.features.storage.local_db.domain.model.Album
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AlbumRepositoryImpl(
    private val database: LocalDatabase,
    private val convertor: AlbumDbConvertor,
): AlbumRepository {

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