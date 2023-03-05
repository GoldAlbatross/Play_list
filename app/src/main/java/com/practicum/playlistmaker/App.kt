package com.practicum.playlistmaker

import android.app.Application
import com.google.gson.Gson
import com.practicum.playlistmaker.storage.ThemeSwitcher

class App: Application() {
    private val gson = Gson()
    internal lateinit var themeSwitcher: ThemeSwitcher

    override fun onCreate() {
        super.onCreate()
        instance = this
        themeSwitcher = ThemeSwitcher(getSharedPreferences(DARK_THEME_PREFERENCES, MODE_PRIVATE))
    }

    companion object {
        const val DARK_THEME_PREFERENCES = "dark_theme"
        lateinit var instance: App
        private set
    }
}