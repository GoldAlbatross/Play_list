package com.practicum.playlistmaker.presenters.search

import com.practicum.playlistmaker.data.Track

class SearchPresenter(
    private val view: SearchView,
    private val router: SearchRouter,
) {
    fun onClickedTrack(track: Track) {
        router.openPlayerActivity(track = track)

    }
}