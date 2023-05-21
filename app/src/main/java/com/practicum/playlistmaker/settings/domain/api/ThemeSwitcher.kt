package com.practicum.playlistmaker.settings.domain.api

interface ThemeSwitcher {
    fun getSwitcherState(): Boolean
    fun saveSwitcherState(state: Boolean)
}