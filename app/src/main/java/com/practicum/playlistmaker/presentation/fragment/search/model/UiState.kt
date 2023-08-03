package com.practicum.playlistmaker.presentation.fragment.search.model

import com.practicum.playlistmaker.domain.network.model.Track

sealed interface UiState{

    object Default: UiState
    object Loading: UiState
    data class SearchContent(val list: List<Track>): UiState
    class NoData(val message: String): UiState
    data class HistoryContent(val list: List<Track>): UiState
    class Error(val message: String): UiState
}