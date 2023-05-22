package com.practicum.playlistmaker.search.data.shared_preferences

import android.content.SharedPreferences
import android.util.Log
import com.practicum.playlistmaker.search.domain.model.Track
import java.lang.reflect.Type
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class LocalStorage(
    private val dataConverter: DataConverter,
    private val sharedPreferences: SharedPreferences
) {

    private val lock = ReentrantReadWriteLock()
    internal fun <T> writeData(key: String, data: T) {
        lock.write {
            when (data) {
                is Boolean, is Byte, is Char, is Short, is Int, is Long, is Float, is Double ->
                    sharedPreferences.edit().putString(key, "$data").apply()
                else ->
                    sharedPreferences.edit().putString(key, dataConverter.dataToJson(data)).apply()
            }
        }
    }

    internal fun <T> readData(key: String, type: Type): T? {
        lock.read {
            val json = sharedPreferences.getString(key, null)
            return if (json == null) null
            else dataConverter.dataFromJson(json, type)
        }
    }
}

