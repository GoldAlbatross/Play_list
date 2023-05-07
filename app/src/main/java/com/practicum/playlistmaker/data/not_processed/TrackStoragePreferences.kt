package com.practicum.playlistmaker.data.not_processed

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.models.domain.Track

class TrackStoragePreferences(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
    private val count: Int
    ): TrackStorage {
    override fun addTrack(track: Track) {
        val list: MutableList<Track> = getJsonString()?.toTrackList() ?: mutableListOf()
        if (list.contains(track)) list.remove(track)
        list.add(0, track)
        if (list.size > count) list.removeAt(9)
        saveList(list)
    }

    override fun getTracks(): List<Track> {
        return getJsonString()?.toTrackList() ?:  listOf()
    }

    override fun clearTrackList() {
        sharedPreferences.edit()
            .remove(TRACKS_KEY)
            .apply()
    }

    override fun removeTrack(track: Track) {
        val list: MutableList<Track> = getJsonString()?.toTrackList() ?: return
        list.remove(track)
        saveList(list)
    }

    private fun getJsonString(): String? = sharedPreferences.getString(TRACKS_KEY, null)
    private fun String.toTrackList(): MutableList<Track> =
        gson.fromJson(this, object : TypeToken<MutableList<Track>>() {}.type)
    private fun saveList(list: List<Track>) {
        sharedPreferences.edit().putString(TRACKS_KEY, gson.toJson(list)).apply()
    }
    companion object { private const val TRACKS_KEY = "tracks" }
}