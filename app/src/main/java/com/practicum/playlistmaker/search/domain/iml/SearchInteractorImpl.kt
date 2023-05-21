package com.practicum.playlistmaker.search.domain.iml

import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.model.Track

class SearchInteractorImpl(
    private val repository: SearchRepository
    ): SearchInteractor {

    override fun removeTrackFromLocalStorage(key: String, track: Track) {
        val tracksFromStorage = getData(key = key).toMutableList()
        tracksFromStorage.remove(track)
        saveData(key = key, list = tracksFromStorage)
    }

    override fun saveTrack(key: String, list: MutableList<Track>, track: Track): List<Track> {
        list.remove(track)
        list.add(0, track)
        if (list.size > maxSizeOfHistoryList) list.removeAt(9)
        saveData(key = key, list = list)
        return list
    }

    override fun getTracksFromLocalStorage(key: String): List<Track> {
        return getData(key = key)
    }

    override fun getTracksFromBackendApi(query: String, consumer: SearchInteractor.TracksConsumer) {
        consumer.consume(repository.searchTracks(query))
    }

    override fun clearTrackList(key: String) {
        repository.saveTrackList(key, mutableListOf())
    }

    private fun getData(key: String): MutableList<Track> {
        return repository.getTracksList(key)
    }
    private fun saveData(key: String, list: MutableList<Track>) {
        repository.saveTrackList(key = key, list = list)
    }

    private companion object { const val maxSizeOfHistoryList = 10 }
}