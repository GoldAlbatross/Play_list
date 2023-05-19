package com.practicum.playlistmaker.search.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson

class LocalStorageImpl(
    private  val gson: Gson,
    private val sharedPreferences: SharedPreferences
    ): LocalStorage {

    override fun <T> saveData(key: String, data: T) {
        sharedPreferences.edit().putString(key, gson.toJson(data)).apply()
    }

    override fun getData(key: String): String? {
        return sharedPreferences.getString(key, null)
    }
}