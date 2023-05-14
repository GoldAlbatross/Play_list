package com.practicum.playlistmaker.search.domain.iml

import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.SearchRepository

class SearchInteractorImpl(private val searchRepository: SearchRepository): SearchInteractor {

    override fun clearHistory() {
        searchRepository.clearTrackList()
    }
}