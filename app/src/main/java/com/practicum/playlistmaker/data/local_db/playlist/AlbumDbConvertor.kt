package com.practicum.playlistmaker.data.local_db.playlist

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.app.TAG
import com.practicum.playlistmaker.domain.network.model.Track
import com.practicum.playlistmaker.domain.local_db.model.Album

class AlbumDbConvertor(
    private val gson: Gson,
) {

    fun map(album: Album): AlbumEntity {
        Log.d(TAG, "AlbumDbConvertor -> map(item: ${album.id}): AlbumEntity")
        return with(album) {
            AlbumEntity(
                id,
                uri,
                name,
                description,
                gson.toJson(trackList),
                trackCount,
                System.currentTimeMillis()
            )
        }
    }

    fun map(albumEntity: AlbumEntity): Album {
        Log.d(TAG, "AlbumDbConvertor -> map(item: ${albumEntity.id}): Album")
        val type = object : TypeToken<List<Track>>() {}.type
        return with(albumEntity) {
            Album(
                id,
                uri,
                name,
                description,
                gson.fromJson(trackList, type),
                trackCount,
            )
        }
    }
}