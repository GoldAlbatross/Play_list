package com.practicum.playlistmaker.search.data.shared_preferences

import java.lang.reflect.Type

interface DataConverter {
    fun <T> dataToJson(data: T): String
    fun <T> dataFromJson(json: String, type: Type): T
}