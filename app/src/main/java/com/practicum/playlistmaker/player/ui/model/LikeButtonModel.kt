package com.practicum.playlistmaker.player.ui.model

sealed interface LikeButtonModel {

    object DisLike: LikeButtonModel
    object Like: LikeButtonModel
}