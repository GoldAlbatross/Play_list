package com.practicum.playlistmaker.features.storage.db_favorite.data

import com.practicum.playlistmaker.features.itunes_api.domain.model.Track

class TrackDbConvertor {

    fun map(item: Track): TrackEntity {
        return with(item) {
            TrackEntity(
                trackId,
                track,
                artist,
                trackTime,
                url,
                collectionName,
                releaseDate,
                primaryGenreName,
                country,
                previewUrl,
                joinDate = System.currentTimeMillis()
            )
        }
    }

    fun map(item: TrackEntity): Track {
        return with(item) {
            Track(
                trackId,
                track,
                artist,
                trackTime,
                url,
                collectionName,
                releaseDate,
                primaryGenreName,
                country,
                previewUrl,
            )
        }
    }
}