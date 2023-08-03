package com.practicum.playlistmaker.data.local_db.favorite

import com.practicum.playlistmaker.domain.network.model.Track

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