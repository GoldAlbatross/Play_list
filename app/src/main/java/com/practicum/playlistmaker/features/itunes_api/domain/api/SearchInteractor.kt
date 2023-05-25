package com.practicum.playlistmaker.features.itunes_api.domain.api

import com.practicum.playlistmaker.features.itunes_api.domain.model.NetworkResponse
import com.practicum.playlistmaker.features.itunes_api.domain.model.Track

interface SearchInteractor {

    fun getTracksFromBackendApi(query: String, consumer: TracksConsumer)

    fun interface TracksConsumer {
        fun consume(response: NetworkResponse<List<Track>>)
    }
}