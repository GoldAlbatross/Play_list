package com.practicum.playlistmaker.search.ui.model

import com.practicum.playlistmaker.search.domain.model.NetworkResponse
import com.practicum.playlistmaker.search.domain.model.Track

sealed interface UiState{

    object Default: UiState

    data class SearchContent(val list: List<Track>): UiState

    data class HistoryContent(val list: List<Track>): UiState

    data class Error(val error: NetworkResponse): UiState

}