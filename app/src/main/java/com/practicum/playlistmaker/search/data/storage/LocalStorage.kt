package com.practicum.playlistmaker.search.data.storage

interface LocalStorage {
    fun <T> saveData(key: String, data: T)
    fun getData(key: String): String?
}