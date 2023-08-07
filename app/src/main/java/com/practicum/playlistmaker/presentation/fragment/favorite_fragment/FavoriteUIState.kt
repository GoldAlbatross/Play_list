package com.practicum.playlistmaker.presentation.fragment.favorite_fragment

import com.practicum.playlistmaker.domain.network.model.Track

sealed interface FavoriteUIState {
    
    object Empty: FavoriteUIState
    data class Success(val data: List<Track>): FavoriteUIState
}