package com.practicum.playlistmaker.search.data.shared_preferences

interface LocalStorage<T> {
    fun writeData(key: String, data: T)
    fun readData(key: String, type: Class<T>): T?
}