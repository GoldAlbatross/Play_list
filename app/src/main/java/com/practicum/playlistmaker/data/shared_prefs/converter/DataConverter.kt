package com.practicum.playlistmaker.data.shared_prefs.converter


interface DataConverter {
    fun <T> dataToJson(data: T): String
    fun <T> dataFromJson(json: String, type: Class<T>): T
}