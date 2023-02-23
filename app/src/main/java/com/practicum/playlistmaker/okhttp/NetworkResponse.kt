package com.practicum.playlistmaker.okhttp

import com.practicum.playlistmaker.model.Track

sealed class NetworkResponse {
    class Success (val listFromApi: List<Track>): NetworkResponse()
    object NoData : NetworkResponse()
    class Error (val message: String): NetworkResponse()
}
