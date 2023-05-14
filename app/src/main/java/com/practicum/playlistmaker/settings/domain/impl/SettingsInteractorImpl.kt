package com.practicum.playlistmaker.settings.domain.impl


import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.api.ThemeSwitcher

class SettingsInteractorImpl(private val repository: ThemeSwitcher): SettingsInteractor {
    override fun saveSwitcherState(isChecked: Boolean) {
        repository.saveSwitcherState(state = isChecked)
    }

    override fun getSwitcherState(): Boolean {
        return repository.getSwitcherState()
    }


}