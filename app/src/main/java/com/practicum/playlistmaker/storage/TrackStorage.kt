package com.practicum.playlistmaker.storage

import com.practicum.playlistmaker.model.Track

interface TrackStorage {
    fun addTracks(tracks: List<Track>)
    fun removeTrack(track: Track)
    fun clearTrackList()
    fun getTracks(): List<Track>
}