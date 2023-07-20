package com.practicum.playlistmaker.features.storage.preferences.data.converter


interface DataConverter {
    fun <T> dataToJson(data: T): String
    fun <T> dataFromJson(json: String, type: Class<T>): T
}