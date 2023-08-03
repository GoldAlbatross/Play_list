package com.practicum.playlistmaker.domain.local_db.model

import com.practicum.playlistmaker.domain.network.model.Track

data class Album(
    val id: Long = 0L,
    val uri: String = "",
    val name: String = "",
    val description: String = "",
    val trackList: List<Track> = emptyList(),
    val trackCount: Int = 0,
)
