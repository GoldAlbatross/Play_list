package com.practicum.playlistmaker.storage

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val SWITCHER_KEY = "switcher"

class ThemeSwitcher(private val sharedPreferences: SharedPreferences): BooleanStorage {

    internal var darkTheme = false
    init { switcherTheme(getBoolean()) }


    override fun addBoolean(state: Boolean) {
        sharedPreferences.edit().putBoolean(SWITCHER_KEY, state).apply()
        switcherTheme(state)
    }


    override fun getBoolean(): Boolean =
        sharedPreferences.getBoolean(SWITCHER_KEY, false)


    internal fun switcherTheme(state: Boolean) {
        darkTheme = state
        AppCompatDelegate.setDefaultNightMode(
            if (state) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}