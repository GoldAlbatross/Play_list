package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.network.dto.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiITunes {
    @GET("/search?entity=song")
    suspend fun getTrackList(@Query("term") text: String): Response<SearchResponse>?
}