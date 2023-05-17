package com.practicum.playlistmaker.player.ui.model

sealed interface AddButtonModel {

    object Add: AddButtonModel
    object Remove: AddButtonModel
}