package com.practicum.playlistmaker.search.domain.iml

import android.util.Log
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.model.Track

class SearchInteractorImpl(
    private val repository: SearchRepository
    ): SearchInteractor {

    override fun removeTrackFromLocalStorage(key: String, track: Track) {
        val tracks = repository.getTracksList(key = key)
        tracks.remove(track)
        repository.saveTrackList(key = key, list = tracks)
    }

    override fun saveTrackAsFirst(key: String, track: Track) {
        val tracks = repository.getTracksList(key = key)
        tracks.remove(track)
        tracks.add(0, track)
        if (tracks.size > maxSizeOfHistoryList) tracks.removeAt(9)
        repository.saveTrackList(key = key, list = tracks)
    }

    override fun getTracksFromLocalStorage(key: String): MutableList<Track> {
        return repository.getTracksList(key = key)
    }

    override fun getTracksFromBackendApi(query: String, consumer: SearchInteractor.TracksConsumer) {
        consumer.consume(repository.searchTracks(query))
    }

    override fun clearTrackList(key: String) {
        repository.saveTrackList(key, mutableListOf())
    }

    private companion object { const val maxSizeOfHistoryList = 10 }
}