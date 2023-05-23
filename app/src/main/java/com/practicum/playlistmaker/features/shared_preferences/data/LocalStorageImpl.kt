package com.practicum.playlistmaker.features.shared_preferences.data

import android.content.SharedPreferences
import com.practicum.playlistmaker.features.shared_preferences.data.converter.DataConverter
import com.practicum.playlistmaker.features.shared_preferences.domain.api.LocalStorage
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class LocalStorageImpl(
    private val dataConverter: DataConverter,
    private val sharedPreferences: SharedPreferences
): LocalStorage {

    private val lock = ReentrantReadWriteLock()
    override fun <T> writeData(key: String, data: T) {
        lock.write {
            when (data) {
                is Boolean ->
                    sharedPreferences.edit().putBoolean(key, data).apply()
                is Int ->
                    sharedPreferences.edit().putInt(key, data).apply()
                else ->
                    sharedPreferences.edit().putString(key, dataConverter.dataToJson(data)).apply()
            }
        }
    }

    override fun <T> readData(key: String, type: Class<T>): T? {
        lock.read {
            return when (type) {
                Boolean::class.java -> sharedPreferences.getBoolean(key, false) as T
                Int::class.java -> sharedPreferences.getInt(key, 0) as T
                else -> {
                    val json = sharedPreferences.getString(key, null)
                    if (json == null) null
                    else dataConverter.dataFromJson(json, type)
                }
            }
        }
    }
}