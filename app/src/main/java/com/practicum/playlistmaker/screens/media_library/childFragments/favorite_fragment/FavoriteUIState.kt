package com.practicum.playlistmaker.screens.media_library.childFragments.favorite_fragment

import com.practicum.playlistmaker.features.itunes_api.domain.model.Track

sealed interface FavoriteUIState {
    
    object Empty: FavoriteUIState
    data class Success(val data: List<Track>): FavoriteUIState
}