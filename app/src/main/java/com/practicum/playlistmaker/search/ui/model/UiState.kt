package com.practicum.playlistmaker.search.ui.model

import com.practicum.playlistmaker.search.domain.model.Track

sealed interface UiState{

    object Default: UiState
    object Loading: UiState
    data class SearchContent(val list: List<Track>): UiState
    data class HistoryContent(val list: List<Track>): UiState
    object Error: UiState
    object NoData: UiState
}