package com.practicum.playlistmaker.domain.local_db.model

sealed interface BottomSheetUIState {

    data class Content(val albums: List<Album>): BottomSheetUIState
    data class Success(val albumName: String): BottomSheetUIState
    data class Exist(val albumName: String): BottomSheetUIState
    object Empty: BottomSheetUIState
}