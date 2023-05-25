package com.practicum.playlistmaker.features.shared_preferences.data

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.reflect.TypeToken
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
                is Boolean -> sharedPreferences.edit().putBoolean(key, data).apply()
                is Int -> sharedPreferences.edit().putInt(key, data).apply()
                else -> sharedPreferences.edit().putString(key, dataConverter.dataToJson(data)).apply()
            }
        }
    }

    override fun <T> readData(key: String, defaultValue: T): T {
        lock.read {
            return when (defaultValue) {
                is Boolean -> sharedPreferences.getBoolean(key, defaultValue as Boolean) as T
                is Int -> sharedPreferences.getInt(key, defaultValue as Int) as T
                else -> sharedPreferences.getArray(key, defaultValue)
            }
        }
    }

    private fun <T> SharedPreferences.getArray(key: String, defaultValue: T): T {
        val json = this.getString(key, null)
        return  if (json == null) defaultValue
        else dataConverter.dataFromJson(json, defaultValue!!::class.java)
    }
}