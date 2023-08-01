package com.practicum.playlistmaker.features.storage.local_db.data.album

import com.practicum.playlistmaker.features.storage.local_db.data.LocalDatabase
import com.practicum.playlistmaker.features.storage.local_db.domain.api.AlbumRepository
import com.practicum.playlistmaker.features.storage.local_db.domain.model.Album
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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
}