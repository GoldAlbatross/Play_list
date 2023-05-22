package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.model.NetworkResponse
import com.practicum.playlistmaker.search.domain.model.Track

interface SearchInteractor {

    fun removeTrackFromLocalStorage(key: String, track: Track)
    fun clearTrackList(key: String)
    fun saveTrackAsFirst(key: String, track: Track)
    fun getTracksFromLocalStorage(key: String): MutableList<Track>
    fun getTracksFromBackendApi(query: String, consumer: TracksConsumer)

    fun interface TracksConsumer {
        fun consume(response: NetworkResponse<List<Track>>)
    }
}