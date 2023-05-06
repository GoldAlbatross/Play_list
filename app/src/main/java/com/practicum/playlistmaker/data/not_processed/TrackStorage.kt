package com.practicum.playlistmaker.data.not_processed

import com.practicum.playlistmaker.models.domain.Track

interface TrackStorage {
    fun addTrack(track: Track)
    fun getTracks(): List<Track>
    fun clearTrackList()
    fun removeTrack(track: Track)

}