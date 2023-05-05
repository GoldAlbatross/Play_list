package com.practicum.playlistmaker.data.raw

import com.practicum.playlistmaker.data.Track

sealed class NetworkResponse {
    class Success (val listFromApi: List<Track>): NetworkResponse()
    object NoData : NetworkResponse()
    class Error (val message: String): NetworkResponse()
}
