package com.practicum.playlistmaker.domain.network.api

import com.practicum.playlistmaker.domain.network.model.NetworkResponse
import com.practicum.playlistmaker.domain.network.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun searchTracks(query: String): Flow<NetworkResponse<List<Track>>>
}