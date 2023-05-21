package com.practicum.playlistmaker.search.data.shared_preferences

import java.lang.reflect.Type

interface DataConverter<T> {
    fun dataToJson(data: T): String
    fun dataFromJson(json: String, type: Type): T
}