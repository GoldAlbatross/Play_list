package com.practicum.playlistmaker.data.local_db.playlist

import com.google.gson.Gson
import com.practicum.playlistmaker.Logger
import com.practicum.playlistmaker.data.local_db.LocalDatabase
import com.practicum.playlistmaker.domain.local_db.api.PlaylistRepository
import com.practicum.playlistmaker.domain.local_db.model.Album
import com.practicum.playlistmaker.utils.className
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val gson: Gson,
    private val database: LocalDatabase,
    private val convertor: AlbumDbConvertor,
    private val logger: Logger
): PlaylistRepository {

    override suspend fun createAlbum(album: Album) {
        logger.log("$className -> createAlbum(album: Album)")
        database.getAlbumDao().createAlbum(convertor.map(album))
    }

    override fun getSavedAlbums(): Flow<List<Album>> {
        logger.log("$className -> getSavedAlbums(): Flow<List<Album>>")
        return database.getAlbumDao()
            .getAlbums()
            .map { album -> album.map { convertor.map(it) }
        }
    }

    override suspend fun removeAlbum(albumId: Long) {
        logger.log("$className -> removeAlbum(albumId: $albumId)")
        database.getAlbumDao().removeAlbumById(albumId)
    }

    override suspend fun refreshAlbum(album: Album) {
        logger.log("$className -> refreshAlbum(album: ${album.id})")
        database.getAlbumDao().updateAlbumFields(
            id = album.id,
            trackList = gson.toJson(album.trackList),
            trackCount = album.trackList.size,
            date = System.currentTimeMillis(),
        )
    }

    override suspend fun updateAlbum(id: Long, uri: String, name: String, description: String) {
        logger.log("$className -> updateAlbum($id, $uri, $name, $description)")
        database.getAlbumDao().updateAlbum(id, uri, name, description)
    }

    override suspend fun getAlbum(id: Long): Album {
        logger.log("$className -> getAlbum(id: $id): Album")
        val albumEntity = database.getAlbumDao().getAlbum(id)
        return convertor.map(albumEntity)
    }
}