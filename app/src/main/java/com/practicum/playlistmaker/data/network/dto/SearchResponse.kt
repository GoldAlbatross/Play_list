package com.practicum.playlistmaker.data.network.dto

import com.google.gson.annotations.SerializedName

class SearchResponse(
    @SerializedName("results")
    val trackList: List<TrackDto>
): Response()