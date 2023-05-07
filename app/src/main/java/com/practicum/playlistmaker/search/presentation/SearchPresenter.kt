package com.practicum.playlistmaker.search.presentation

import com.practicum.playlistmaker.models.domain.Track

class SearchPresenter(
    private val view: SearchView,
    private val router: SearchRouter,
) {
    fun onClickedTrack(track: Track) {
        router.openPlayerActivity(track = track)

    }
}