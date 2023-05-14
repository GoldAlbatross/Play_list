package com.practicum.playlistmaker.settings.data

import android.content.SharedPreferences
import com.practicum.playlistmaker.settings.domain.api.ThemeSwitcher

class ThemeSwitcherImpl(private val sharedPreferences: SharedPreferences): ThemeSwitcher {

    override fun getSwitcherState(): Boolean =
        sharedPreferences.getBoolean(DARK_THEME_KEY, false)

    override fun saveSwitcherState(state: Boolean) {
        sharedPreferences.edit().putBoolean(DARK_THEME_KEY, state).apply()
    }

    private companion object { const val DARK_THEME_KEY = "dark_theme" }
}