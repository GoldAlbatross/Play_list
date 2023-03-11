package com.practicum.playlistmaker.storage

import android.util.LruCache
import com.practicum.playlistmaker.model.Track

interface TrackStorage {
    fun addTrack(track: Track)
    fun getTracks(): List<Track>
    fun clearTrackList()

}