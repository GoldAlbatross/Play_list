package com.practicum.playlistmaker.screens.player.model

sealed interface LikeButtonModel {

    object DisLike: LikeButtonModel
    object Like: LikeButtonModel
}