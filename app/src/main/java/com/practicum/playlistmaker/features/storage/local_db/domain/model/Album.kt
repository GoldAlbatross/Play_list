package com.practicum.playlistmaker.features.storage.local_db.domain.model

import com.practicum.playlistmaker.features.itunes_api.domain.model.Track
import java.net.URI

data class Album(
    val id: Long = 0L,
    var uri: String = "",
    var name: String = "",
    var description: String = "",
    val trackList: List<Track> = emptyList(),
    val trackCount: Int = 0,
)
