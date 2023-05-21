package com.practicum.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName

class SearchResponse(
    @SerializedName("results")
    val trackList: List<TrackDto>
): Response()