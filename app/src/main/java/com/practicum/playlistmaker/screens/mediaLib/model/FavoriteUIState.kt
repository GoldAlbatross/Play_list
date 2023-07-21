package com.practicum.playlistmaker.screens.mediaLib.model

import com.practicum.playlistmaker.features.itunes_api.domain.model.Track

sealed interface FavoriteUIState {
    
    object Empty: FavoriteUIState
    data class Success(val data: List<Track>): FavoriteUIState
}