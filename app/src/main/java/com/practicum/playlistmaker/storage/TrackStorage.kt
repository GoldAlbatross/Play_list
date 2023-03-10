package com.practicum.playlistmaker.storage

import android.util.LruCache
import com.practicum.playlistmaker.model.Track

interface TrackStorage {
    fun addTracks(track: Track)
    fun getTracks(): List<Track>
    fun clearTrackList()

}