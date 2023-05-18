package com.practicum.playlistmaker.search.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.domain.model.Track

class LocalStorageImpl(
    private  val gson: Gson,
    private val sharedPreferences: SharedPreferences
    ): LocalStorage {

    override fun saveData(key: String, list: List<Track>) {
        saveList(key = key,list = list)
    }
    override fun getData(key: String): MutableList<Track> {
        return getJsonString(key = key)?.toTrackList() ?:  mutableListOf()
    }


    private fun getJsonString(key: String): String? = sharedPreferences.getString(key, null)
    private fun String.toTrackList(): MutableList<Track> =
        gson.fromJson(this, object : TypeToken<MutableList<Track>>() {}.type)
    private fun saveList(key: String, list: List<Track>) {
        sharedPreferences.edit().putString(key, gson.toJson(list)).apply()
    }
}