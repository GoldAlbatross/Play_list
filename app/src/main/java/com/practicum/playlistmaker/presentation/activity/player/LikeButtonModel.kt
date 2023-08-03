package com.practicum.playlistmaker.presentation.activity.player

sealed interface LikeButtonModel {

    object DisLike: LikeButtonModel
    object Like: LikeButtonModel
}