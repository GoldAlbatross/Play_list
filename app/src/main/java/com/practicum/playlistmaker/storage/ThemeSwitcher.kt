package com.practicum.playlistmaker.storage

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class ThemeSwitcher(private val sharedPreferences: SharedPreferences): BooleanStorage {

    init { switcherTheme(getBoolean()) }

    override fun addBoolean(state: Boolean) {
        sharedPreferences.edit().putBoolean(DARK_THEME_KEY, state).apply()
        switcherTheme(state)
    }

    override fun getBoolean(): Boolean =
        sharedPreferences.getBoolean(DARK_THEME_KEY, false)

    private fun switcherTheme(state: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (state) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
    companion object {
        private const val DARK_THEME_KEY = "dark_theme"
    }
}