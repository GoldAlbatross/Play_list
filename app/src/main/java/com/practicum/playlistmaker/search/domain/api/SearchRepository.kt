package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.model.NetworkResponse
import com.practicum.playlistmaker.search.domain.model.Track

interface SearchRepository {

    fun searchTracks(query: String): NetworkResponse<List<Track>>
    fun getTracks(key: String): MutableList<Track>
    fun clearTrackList(key: String)
    fun saveData(key: String, list: MutableList<Track>)
}