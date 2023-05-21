package com.practicum.playlistmaker.search.data.shared_preferences

import android.content.SharedPreferences
import java.lang.reflect.Type

class LocalStorage<T>(
    private val dataConverter: DataConverter<T>,
    private val sharedPreferences: SharedPreferences
) {


    internal fun writeData(key: String, data: T) {
        synchronized(key) {
            when (data) {
                is Boolean, is Byte, is Char, is Short, is Int, is Long, is Float, is Double ->
                    sharedPreferences.edit().putString(key, "$data").apply()
                else ->
                    sharedPreferences.edit().putString(key, dataConverter.dataToJson(data)).apply()
            }
        }
    }

    internal fun readData(key: String, type: Type): T? {
        synchronized(key) {
            val json = sharedPreferences.getString(key, null)
            return if (json == null) null
            else dataConverter.dataFromJson(json, type)
        }
    }
}

