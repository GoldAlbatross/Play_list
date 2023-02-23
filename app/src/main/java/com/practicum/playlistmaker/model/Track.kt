package com.practicum.playlistmaker.model

import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("trackName")
    val track: String,
    @SerializedName("artistName")
    val artist: String,
    @SerializedName("trackTimeMillis")
    val trackTime: Int,
    @SerializedName("artworkUrl100")
    val url: String
)

class TrackResponse(@SerializedName("results") val trackList: List<Track>)
