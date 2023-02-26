package com.practicum.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
const val PLAY_LIST_PREFERENCES = "play_list_preferences"
const val SWITCH_KEY = "switch_key"

class App: Application() {
    internal var darkTheme: Boolean  = false
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(PLAY_LIST_PREFERENCES, Context.MODE_PRIVATE)
        switcherTheme(sharedPrefs.getBoolean(SWITCH_KEY, false))
    }

    internal fun switcherTheme(state: Boolean) {
        darkTheme = state
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    internal fun saveTheme(state: Boolean) {
        sharedPrefs.edit().putBoolean(SWITCH_KEY, state).apply()
    }
}