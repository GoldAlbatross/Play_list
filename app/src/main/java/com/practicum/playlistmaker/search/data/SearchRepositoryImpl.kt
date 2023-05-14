package com.practicum.playlistmaker.search.data

import android.content.SharedPreferences
import com.practicum.playlistmaker.search.domain.api.SearchRepository

class SearchRepositoryImpl(private val sharedPreferences: SharedPreferences): SearchRepository {

    override fun clearTrackList() {
        sharedPreferences.edit()
            .remove(TRACKS_KEY)
            .apply()
    }

    companion object { private const val TRACKS_KEY = "tracks" }
}