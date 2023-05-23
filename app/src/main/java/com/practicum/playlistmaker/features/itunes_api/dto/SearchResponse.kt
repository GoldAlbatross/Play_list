package com.practicum.playlistmaker.features.itunes_api.dto

import com.google.gson.annotations.SerializedName

class SearchResponse(
    @SerializedName("results")
    val trackList: List<TrackDto>
): Response()