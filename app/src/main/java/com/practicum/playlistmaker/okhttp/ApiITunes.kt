package com.practicum.playlistmaker.okhttp

import com.practicum.playlistmaker.model.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiITunes {
    @GET("/search?entity=song")
    fun getTrackList(
        @Query("term") text: String
    ): Call<TrackResponse>
}