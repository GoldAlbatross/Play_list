package com.practicum.playlistmaker.presentation.activity.player

sealed interface PlayerState {

    data class Loading(val message: String? = null): PlayerState
    data class Error(val message: String = ""): PlayerState
    object Ready: PlayerState
    object Play: PlayerState
    object Pause: PlayerState
}