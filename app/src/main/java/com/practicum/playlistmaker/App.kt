package com.practicum.playlistmaker

import android.app.Application
import com.google.gson.Gson
import com.practicum.playlistmaker.storage.BooleanStorage
import com.practicum.playlistmaker.storage.ThemeSwitcher
import com.practicum.playlistmaker.storage.TrackStorage
import com.practicum.playlistmaker.storage.TrackStoragePreferences

class App: Application() {
    internal val gson = Gson()
    internal lateinit var themeSwitcher: BooleanStorage
    private set
    internal lateinit var trackStorage: TrackStorage
    private set


    override fun onCreate() {
        super.onCreate()
        instance = this
        themeSwitcher =
            ThemeSwitcher(getSharedPreferences(DARK_THEME_PREFERENCES, MODE_PRIVATE))
        trackStorage =
            TrackStoragePreferences(getSharedPreferences(TRACKS_PREFERENCES, MODE_PRIVATE), gson, 10)
    }

    companion object {
        const val DARK_THEME_PREFERENCES = "dark_theme_preferences"
        const val TRACKS_PREFERENCES = "tracks_preferences"
        lateinit var instance: App
        private set
    }
}