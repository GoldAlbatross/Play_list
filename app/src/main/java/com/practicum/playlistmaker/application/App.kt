package com.practicum.playlistmaker.application

import android.app.Application
import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.TrackStorage
import com.practicum.playlistmaker.TrackStoragePreferences

class App: Application() {

    internal lateinit var sharedPreference: SharedPreferences
    private set
    internal lateinit var trackStorage: TrackStorage
    private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        sharedPreference = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)

        trackStorage = TrackStoragePreferences(
            getSharedPreferences(TRACKS_PREFERENCES, MODE_PRIVATE),
            Gson(), 10
        )
    }

    companion object {
        private const val APP_PREFERENCES = "app_preferences"
        const val TRACKS_PREFERENCES = "tracks_preferences"
        lateinit var instance: App
        private set
    }
}