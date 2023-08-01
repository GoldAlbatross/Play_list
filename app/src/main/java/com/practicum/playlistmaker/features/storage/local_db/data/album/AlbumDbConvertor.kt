package com.practicum.playlistmaker.features.storage.local_db.data.album

import com.practicum.playlistmaker.features.storage.local_db.domain.model.Album
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AlbumDbConvertor {

    fun map(item: Album): AlbumEntity {
        return with(item) {
            AlbumEntity(
                id,
                uri,
                name,
                description,
                Json.encodeToString(trackList),
                trackCount,
                System.currentTimeMillis()
            )
        }
    }

    fun map(item: AlbumEntity): Album {
        return with(item) {
            Album(
                id,
                uri,
                name,
                description,
                Json.decodeFromString(trackList),
                trackCount,
            )
        }
    }
}