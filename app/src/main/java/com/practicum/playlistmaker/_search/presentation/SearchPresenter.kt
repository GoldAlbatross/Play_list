package com.practicum.playlistmaker._search.presentation

import com.practicum.playlistmaker._player.domain.model.Track

class SearchPresenter(
    private val view: _SearchView,
    private val router: SearchRouter,
) {
    fun onClickedTrack(track: Track) {
        router.openPlayerActivity(track = track)

    }
}