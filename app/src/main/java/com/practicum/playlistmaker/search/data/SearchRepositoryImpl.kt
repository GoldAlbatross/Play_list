package com.practicum.playlistmaker.search.data

import android.content.Context
import android.util.Log
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.SearchRequest
import com.practicum.playlistmaker.search.data.dto.SearchResponse
import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.shared_preferences.LocalStorage
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.model.NetworkResponse
import com.practicum.playlistmaker.search.domain.model.Track

class SearchRepositoryImpl(
    private val localStorage: LocalStorage,
    private val networkClient: NetworkClient,
    private val context: Context,
): SearchRepository {

    override fun getTracksList(key: String): MutableList<Track> {
        return localStorage.readData(
            key = key,
            type = object : TypeToken<MutableList<Track>>() {}.type
        ) ?: mutableListOf()
    }

    override fun saveTrackList(key: String, list: MutableList<Track>) {
        localStorage.writeData(key = key, data = list)
    }

    override fun searchTracks(query: String): NetworkResponse<List<Track>> {
        val response = networkClient.getResponseFromBackend(SearchRequest(query = query))
        return when (response.resultCode) {
            200 -> checkNonEmptyData(response)
            -1 -> NetworkResponse.Offline(message = context.getString(R.string.error))
            in (400..500) -> NetworkResponse.NoData(message = context.getString(R.string.empty_list))
            else -> NetworkResponse.Error(message = context.getString(R.string.server_error))
        }
    }

    private fun checkNonEmptyData(response: Response): NetworkResponse<List<Track>> {
        val list = (response as SearchResponse).trackList.filter {it.previewUrl != null}
        val trackList = list.map { mapToTrack(it) }
        return if (trackList.isEmpty())
            NetworkResponse.NoData(message = context.getString(R.string.empty_list))
        else
            NetworkResponse.Success(trackList)
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