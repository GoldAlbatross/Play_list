package com.practicum.playlistmaker.data.local_db.playlist

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "album")
class AlbumEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val uri: String,
    val name: String,
    val description: String,
    val trackList: String,
    val trackCount: Int,
    val date: Long,
)