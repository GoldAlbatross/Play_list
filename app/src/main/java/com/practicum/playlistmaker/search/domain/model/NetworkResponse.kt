package com.practicum.playlistmaker.search.domain.model

sealed class NetworkResponse {
    class Success (val listFromApi: List<Track>): NetworkResponse()
    object NoData : NetworkResponse()
    class Error (val message: String): NetworkResponse()
}
