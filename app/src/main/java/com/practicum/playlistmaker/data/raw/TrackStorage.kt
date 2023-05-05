package com.practicum.playlistmaker.data.raw

import com.practicum.playlistmaker.data.Track

interface TrackStorage {
    fun addTrack(track: Track)
    fun getTracks(): List<Track>
    fun clearTrackList()
    fun removeTrack(track: Track)

}