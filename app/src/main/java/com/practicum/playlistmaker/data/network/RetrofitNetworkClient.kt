package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.network.dto.Response
import com.practicum.playlistmaker.data.network.dto.SearchRequest

class RetrofitNetworkClient(
    private val backendApi: ApiITunes,
    private val netChecker: InternetController,
): NetworkClient {


    override suspend fun getResponseFromBackend(dto: Any): Response {
        if (!netChecker.isInternetAvailable()) return Response().apply { resultCode = -1 }

        if (dto !is SearchRequest) return Response().apply { resultCode = 400 }

        val response = backendApi.getTrackList(dto.query)
        return response?.body()?.apply { resultCode = response.code() }
            ?: Response().apply { resultCode = response?.code() ?: 400 }
    }

}