package com.practicum.playlistmaker.features.itunes_api.data.network

import com.practicum.playlistmaker.features.itunes_api.dto.SearchResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiITunes {
    @GET("/search?entity=song")
    suspend fun getTrackList(@Query("term") text: String): Response<SearchResponse>?
}