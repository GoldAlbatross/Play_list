package com.practicum.playlistmaker.features.storage.domain.api

interface LocalStorage {

    fun <T> writeData(key: String, data: T)
    fun <T> readData(key: String, defaultValue: T): T
}