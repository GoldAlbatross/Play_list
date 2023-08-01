package com.practicum.playlistmaker.features.storage.local_db.domain.model

sealed interface ScreenState {

    data class Content(val albums: List<Album>): ScreenState
    object Empty: ScreenState
}