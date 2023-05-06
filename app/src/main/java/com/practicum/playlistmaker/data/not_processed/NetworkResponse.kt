package com.practicum.playlistmaker.data.not_processed

import com.practicum.playlistmaker._player.domain.model.Track

sealed class NetworkResponse {
    class Success (val listFromApi: List<Track>): NetworkResponse()
    object NoData : NetworkResponse()
    class Error (val message: String): NetworkResponse()
}
