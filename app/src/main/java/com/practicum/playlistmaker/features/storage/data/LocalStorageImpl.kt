package com.practicum.playlistmaker.features.storage.data

import android.content.SharedPreferences
import com.practicum.playlistmaker.features.storage.data.converter.DataConverter
import com.practicum.playlistmaker.features.storage.domain.api.LocalStorage
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class LocalStorageImpl(
    private val dataConverter: DataConverter,
    private val preferences: SharedPreferences
): LocalStorage {


    private val lock = ReentrantReadWriteLock()
    override fun <T> writeData(key: String, data: T) {
        lock.write {
            when (data) {
                is Boolean -> preferences.edit().putBoolean(key, data).apply()
                is Int -> preferences.edit().putInt(key, data).apply()
                is String -> preferences.edit().putString(key, data).apply()
                else -> preferences.edit().putString(key, dataConverter.dataToJson(data)).apply()
            }
        }
    }

    override fun <T> readData(key: String, defaultValue: T): T {
        lock.read {
            return when (defaultValue) {
                is Boolean -> preferences.getBoolean(key, defaultValue as Boolean) as T
                is Int -> preferences.getInt(key, defaultValue as Int) as T
                is String -> preferences.getString(key, defaultValue) as T
                else -> preferences.getObject(key, defaultValue)
            }
        }
    }

    private fun <T> SharedPreferences.getObject(key: String, defaultValue: T): T {
        val json = this.getString(key, null)
        return  if (json == null || json == "null")
            defaultValue
        else
            dataConverter.dataFromJson(json, defaultValue!!::class.java)
    }
}