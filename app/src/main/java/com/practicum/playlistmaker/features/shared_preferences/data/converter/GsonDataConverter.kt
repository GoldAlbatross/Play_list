package com.practicum.playlistmaker.features.shared_preferences.data.converter

import com.google.gson.Gson
import java.lang.reflect.Type

class GsonDataConverter(
    private val gson: Gson,
) : DataConverter {


    override fun <T> dataToJson(data: T): String =
        gson.toJson(data)

    override fun <T> dataFromJson(json: String, type: Class<T>): T =
        gson.fromJson(json, type)
}