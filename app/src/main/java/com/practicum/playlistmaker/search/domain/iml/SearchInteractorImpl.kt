package com.practicum.playlistmaker.search.domain.iml

import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.model.Track
import java.util.concurrent.Executors

class SearchInteractorImpl(
    private val repository: SearchRepository
    ): SearchInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun removeTrackFromLocalStorage(key: String, track: Track) {
        val tracksFromStorage = getData(key = key)
        tracksFromStorage.remove(track)
        saveData(key = key, list = tracksFromStorage)
    }

    override fun saveTrack(key: String, list: MutableList<Track>, track: Track): List<Track> {
        list.remove(track)
        list.add(0, track)
        if (list.size > maxSizeOfHistoryList) list.removeAt(9)
        repository.saveData(key = key, list = list)
        return list
    }

    override fun getTracksFromLocalStorage(key: String): List<Track> {
        return getData(key = key)
    }

    override fun getTracksFromApi(query: String, consumer: SearchInteractor.TracksConsumer) {
        executor.execute {
            val response = repository.searchTracks(query)
            consumer.consume(response)
        }
    }

    override fun clearTrackList(key: String) {
        repository.clearTrackList(key)
    }
    private fun getData(key: String): MutableList<Track> {
        return repository.getTracks(key)
    }
    private fun saveData(key: String, list: MutableList<Track>) {
        repository.saveData(key = key, list = list)
    }

    private companion object { const val maxSizeOfHistoryList = 10 }
}