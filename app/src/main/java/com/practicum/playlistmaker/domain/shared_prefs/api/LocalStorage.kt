package com.practicum.playlistmaker.domain.shared_prefs.api

interface LocalStorage {

    fun <T> writeData(key: String, data: T)
    fun <T> readData(key: String, defaultValue: T): T
}