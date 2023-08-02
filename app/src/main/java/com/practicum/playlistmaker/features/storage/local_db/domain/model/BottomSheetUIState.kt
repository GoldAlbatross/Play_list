package com.practicum.playlistmaker.features.storage.local_db.domain.model

sealed interface BottomSheetUIState {

    data class Content(val albums: List<Album>): BottomSheetUIState
    data class Success(val albumName: String): BottomSheetUIState
    data class Exist(val albumName: String): BottomSheetUIState
    object Empty: BottomSheetUIState
}