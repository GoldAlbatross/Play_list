package com.practicum.playlistmaker.features.itunes_api.domain.api

import com.practicum.playlistmaker.features.itunes_api.domain.model.NetworkResponse
import com.practicum.playlistmaker.features.itunes_api.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun searchTracks(query: String): Flow<NetworkResponse<List<Track>>>
}