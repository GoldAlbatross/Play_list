package com.practicum.playlistmaker.features.itunes_api.domain.api

import com.practicum.playlistmaker.features.itunes_api.domain.model.NetworkResponse
import com.practicum.playlistmaker.features.itunes_api.domain.model.Track

interface SearchRepository {

    fun searchTracks(query: String): NetworkResponse<List<Track>>
}