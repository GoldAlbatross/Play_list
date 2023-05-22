package com.practicum.playlistmaker.search.data.shared_preferences

import com.google.gson.Gson
import java.lang.reflect.Type

class GsonDataConverter(private val gson: Gson, ) : DataConverter {

    override fun <T> dataToJson(data: T): String =
        gson.toJson(data)

    override fun <T> dataFromJson(json: String, type: Type): T =
        gson.fromJson(json, type)
}