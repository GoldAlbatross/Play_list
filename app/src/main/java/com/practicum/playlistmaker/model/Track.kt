package com.practicum.playlistmaker.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Track(
    val trackId: Int,
    @SerializedName("trackName")
    val track: String,
    @SerializedName("artistName")
    val artist: String,
    @SerializedName("trackTimeMillis")
    val trackTime: Int,
    @SerializedName("artworkUrl100")
    val url: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
): Serializable

class TrackResponse(@SerializedName("results") val trackList: List<Track>)
