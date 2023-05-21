package com.practicum.playlistmaker.search.data.shared_preferences

import android.content.SharedPreferences
import com.google.gson.Gson

class LocalStorageImpl<T>(
    private  val gson: Gson,
    private val sharedPreferences: SharedPreferences
): LocalStorage<T> {

    override fun writeData(key: String, data: T) {
        synchronized(key) {
            sharedPreferences.edit().putString(key, gson.toJson(data)).apply()
        }
    }

    override fun readData(key: String, type: Class<T>): T? {
        synchronized(key) {
            val json = sharedPreferences.getString(key, null)
            return if (json == null) null
            else gson.fromJson(json, type)
        }
    }
}

