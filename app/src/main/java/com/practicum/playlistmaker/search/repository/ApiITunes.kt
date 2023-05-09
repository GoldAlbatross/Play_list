package com.practicum.playlistmaker.search.repository

import com.practicum.playlistmaker.models.data.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiITunes {
    @GET("/search?entity=song")
    fun getTrackList(
        @Query("term") text: String
    ): Call<TrackResponse>
}