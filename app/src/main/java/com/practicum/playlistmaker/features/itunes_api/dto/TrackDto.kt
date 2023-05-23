package com.practicum.playlistmaker.features.itunes_api.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class TrackDto(
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
    val country: String,
    val previewUrl: String?,
): Parcelable
