package com.practicum.playlistmaker.features.itunes_api.data.network

import com.practicum.playlistmaker.features.itunes_api.dto.Response

interface NetworkClient {
    fun getResponseFromBackend(dto: Any): Response
}