package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.network.dto.Response

interface NetworkClient {
    suspend fun getResponseFromBackend(dto: Any): Response
}