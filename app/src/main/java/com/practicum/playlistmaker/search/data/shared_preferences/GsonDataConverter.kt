package com.practicum.playlistmaker.search.data.shared_preferences

import com.google.gson.Gson
import java.lang.reflect.Type

class GsonDataConverter<T>(private val gson: Gson, ) : DataConverter<T> {

    override fun dataToJson(data: T): String =
        gson.toJson(data)

    override fun dataFromJson(json: String, type: Type): T =
        gson.fromJson(json, type)
}