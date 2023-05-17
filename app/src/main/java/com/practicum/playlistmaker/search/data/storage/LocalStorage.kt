package com.practicum.playlistmaker.search.data.storage

import com.practicum.playlistmaker.search.domain.model.Track

interface LocalStorage {

    fun addTrack(key: String, list: List<Track>)
    fun clearTrackList(key: String)
    fun getTracks(key: String): MutableList<Track>
}