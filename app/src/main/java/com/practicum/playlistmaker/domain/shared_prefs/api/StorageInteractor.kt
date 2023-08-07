package com.practicum.playlistmaker.domain.shared_prefs.api

import com.practicum.playlistmaker.domain.network.model.Track

interface StorageInteractor {
    fun saveSwitcherState(key: String, themeState: Boolean)
    fun getSwitcherState (key: String): Boolean
    fun removeTrackFromLocalStorage(key: String, track: Track)
    fun saveTrackAsFirstToLocalStorage(key: String, track: Track)
    fun getTracksFromLocalStorage(key: String): MutableList<Track>
    fun clearTrackList(key: String)

}