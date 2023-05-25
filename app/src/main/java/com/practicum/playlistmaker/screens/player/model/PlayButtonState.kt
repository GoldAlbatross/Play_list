package com.practicum.playlistmaker.screens.player.model

sealed interface PlayButtonState {

    class Prepare(val clicked: Boolean): PlayButtonState
    object PrepareDone: PlayButtonState
    object Play: PlayButtonState
    object Pause: PlayButtonState
}