package com.practicum.playlistmaker.features.storage.local_db.data.favorite

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "favorite_tracks")
data class TrackEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey
    val trackId: Int,
    val track: String,
    val artist: String,
    val trackTime: Int,
    val url: String,
    val collectionName: String,
    val releaseDate: String,
    @ColumnInfo(name = "genre")
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val joinDate: Long,
)