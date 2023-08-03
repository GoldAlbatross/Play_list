package com.practicum.playlistmaker.domain.local_db.model

sealed interface ScreenState {

    data class Content(val albums: List<Album>): ScreenState
    object Empty: ScreenState
}