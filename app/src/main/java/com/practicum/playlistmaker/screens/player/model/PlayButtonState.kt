package com.practicum.playlistmaker.screens.player.model

sealed interface PlayButtonState {

    data class Loading(val message: String? = null): PlayButtonState
    data class Error(val message: String): PlayButtonState
    object PrepareDone: PlayButtonState
    object Play: PlayButtonState
    object Pause: PlayButtonState
}