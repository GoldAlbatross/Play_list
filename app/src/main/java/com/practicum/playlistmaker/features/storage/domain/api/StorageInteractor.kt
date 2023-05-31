package com.practicum.playlistmaker.features.storage.domain.api

import com.practicum.playlistmaker.features.itunes_api.domain.model.Track

interface StorageInteractor {
    fun saveSwitcherState(key: String, themeState: Boolean)
    fun getSwitcherState (key: String): Boolean
    fun removeTrackFromLocalStorage(key: String, track: Track)
    fun saveTrackAsFirstToLocalStorage(key: String, track: Track)
    fun getTracksFromLocalStorage(key: String): MutableList<Track>
    fun clearTrackList(key: String)

}