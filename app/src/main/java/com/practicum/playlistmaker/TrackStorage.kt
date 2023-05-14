package com.practicum.playlistmaker

import com.practicum.playlistmaker.search.domain.model.Track

interface TrackStorage {
    fun addTrack(track: Track)
    fun getTracks(): List<Track>
    fun clearTrackList()
    fun removeTrack(track: Track)

}