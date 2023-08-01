package com.practicum.playlistmaker.screens.album_creating

sealed interface ScreenState {

    object Default: ScreenState
    object Dialog: ScreenState
    data class Button(val clickable: Boolean): ScreenState
    object GoBack: ScreenState
    data class SaveAlbum(val name: String): ScreenState
}