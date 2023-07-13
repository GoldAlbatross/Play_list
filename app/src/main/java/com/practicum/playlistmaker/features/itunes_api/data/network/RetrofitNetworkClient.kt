package com.practicum.playlistmaker.features.itunes_api.data.network

import com.practicum.playlistmaker.features.itunes_api.dto.Response
import com.practicum.playlistmaker.features.itunes_api.dto.SearchRequest

class RetrofitNetworkClient(
    private val backendApi: ApiITunes,
    private val netChecker: InternetController,
): NetworkClient {


    override fun getResponseFromBackend(dto: Any): Response {
        if (!netChecker.isInternetAvailable()) return Response().apply { resultCode = -1 }

        if (dto !is SearchRequest) return Response().apply { resultCode = 400 }

        val response = backendApi.getTrackList(dto.query).execute()
        return response.body()?.apply { resultCode = response.code() }
            ?: Response().apply { resultCode = response.code() }
    }

}