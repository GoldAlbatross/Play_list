package com.practicum.playlistmaker.features.storage.local_db.data.album

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.practicum.playlistmaker.features.itunes_api.domain.model.Track

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