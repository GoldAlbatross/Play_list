package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.SearchRequest
import com.practicum.playlistmaker.search.data.dto.SearchResponse
import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.storage.LocalStorage
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.model.NetworkResponse
import com.practicum.playlistmaker.search.domain.model.Track

class SearchRepositoryImpl(
    private val localStorage: LocalStorage,
    private val networkClient: NetworkClient,
): SearchRepository {


    override fun searchTracks(query: String): NetworkResponse<List<Track>> {

        val response = networkClient.getResponseFromBackend(SearchRequest(query = query))
        return when (response.resultCode) {
            200 -> {
                val list = (response as SearchResponse).trackList.filter {it.previewUrl != null}
                NetworkResponse.Success(list.map { mapToTrack(it) })
            }
            -1 -> { NetworkResponse.Offline("Проверьте подключение к интернету") }
            else -> { NetworkResponse.Error("Ошибка сервера") }
        }
    }

    override fun getTracks(key: String): MutableList<Track> {
        return localStorage.getData(key)
    }

    override fun clearTrackList(key: String) {
        localStorage.saveData(key, emptyList())
    }

    override fun saveData(key: String, list: MutableList<Track>) {
        localStorage.saveData(key = key, list = list)
    }

    private fun mapToTrack(trackDto: TrackDto): Track {
        return Track(
            track = trackDto.track,
            artist = trackDto.artist,
            trackTime = trackDto.trackTime,
            url = trackDto.url,
            collectionName = trackDto.collectionName,
            releaseDate = trackDto.releaseDate,
            primaryGenreName = trackDto.primaryGenreName,
            country = trackDto.country,
            previewUrl = trackDto.previewUrl!!,
        )
    }
}