package com.practicum.playlistmaker.settings.domain.api

interface SettingsInteractor {
    fun saveSwitcherState (isChecked: Boolean)
    fun getSwitcherState (): Boolean

}