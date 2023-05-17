package com.practicum.playlistmaker.player.ui.model

sealed interface PlayButtonModel {

    object Prepare: PlayButtonModel
    object PrepareDone: PlayButtonModel
    object Play: PlayButtonModel
    object Pause: PlayButtonModel
}