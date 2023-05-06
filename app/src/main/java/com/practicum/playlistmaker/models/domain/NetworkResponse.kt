package com.practicum.playlistmaker.models.domain

sealed class NetworkResponse {
    class Success (val listFromApi: List<Track>): NetworkResponse()
    object NoData : NetworkResponse()
    class Error (val message: String): NetworkResponse()
}
