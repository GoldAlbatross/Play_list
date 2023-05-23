package com.practicum.playlistmaker.screens.search.model

import com.practicum.playlistmaker.features.itunes_api.domain.model.Track

sealed interface UiState{

    object Default: UiState
    object Loading: UiState
    data class SearchContent(val list: List<Track>): UiState
    class NoData(val message: String): UiState
    data class HistoryContent(val list: List<Track>): UiState
    class Error(val message: String): UiState
}