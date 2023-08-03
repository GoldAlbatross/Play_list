package com.practicum.playlistmaker.domain.network.iml

import com.practicum.playlistmaker.domain.network.api.SearchInteractor
import com.practicum.playlistmaker.domain.network.api.SearchRepository
import com.practicum.playlistmaker.domain.network.model.NetworkResponse
import com.practicum.playlistmaker.domain.network.model.Track
import kotlinx.coroutines.flow.Flow

class SearchInteractorImpl(
    private val repository: SearchRepository
    ): SearchInteractor {

    override fun getTracksFromBackendApi(query: String): Flow<NetworkResponse<List<Track>>> {
        return repository.searchTracks(query)
    }

}