package com.practicum.playlistmaker.data.network

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.network.dto.Response
import com.practicum.playlistmaker.data.network.dto.SearchRequest
import com.practicum.playlistmaker.data.network.dto.SearchResponse
import com.practicum.playlistmaker.data.network.dto.TrackDto
import com.practicum.playlistmaker.domain.network.api.SearchRepository
import com.practicum.playlistmaker.domain.network.model.NetworkResponse
import com.practicum.playlistmaker.domain.network.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val context: Context,
): SearchRepository {

    override fun searchTracks(query: String): Flow<NetworkResponse<List<Track>>> = flow {
        val response = networkClient.getResponseFromBackend(SearchRequest(query = query))
        emit (when (response.resultCode) {
            200 -> checkNonEmptyData(response)
            -1 -> NetworkResponse.Offline(message = context.getString(R.string.error))
            in (400..500) -> NetworkResponse.NoData(message = context.getString(R.string.empty_list))
            else -> NetworkResponse.Error(message = context.getString(R.string.server_error))
        })
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
            trackId = trackDto.trackId,
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