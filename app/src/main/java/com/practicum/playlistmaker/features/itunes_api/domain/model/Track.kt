package com.practicum.playlistmaker.features.itunes_api.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Track(
    val trackId: Int,
    val track: String,
    val artist: String,
    val trackTime: Int,
    val url: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
): Parcelable
