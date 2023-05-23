package com.practicum.playlistmaker.features.itunes_api.domain.iml

import com.practicum.playlistmaker.features.itunes_api.domain.api.SearchInteractor
import com.practicum.playlistmaker.features.itunes_api.domain.api.SearchRepository
import com.practicum.playlistmaker.features.itunes_api.domain.model.Track
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.write

class SearchInteractorImpl(
    private val repository: SearchRepository
    ): SearchInteractor {

    override fun getTracksFromBackendApi(query: String, consumer: SearchInteractor.TracksConsumer) {
        consumer.consume(repository.searchTracks(query))
    }

}