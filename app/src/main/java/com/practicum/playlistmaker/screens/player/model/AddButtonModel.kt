package com.practicum.playlistmaker.screens.player.model

sealed interface AddButtonModel {

    object Add: AddButtonModel
    object Remove: AddButtonModel
}