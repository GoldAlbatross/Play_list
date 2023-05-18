package com.practicum.playlistmaker.search.data.storage

import com.practicum.playlistmaker.search.domain.model.Track

interface LocalStorage {
    fun saveData(key: String, list: List<Track>)
    fun getData(key: String): MutableList<Track>
}