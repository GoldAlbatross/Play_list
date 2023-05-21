package com.practicum.playlistmaker.main.ui.view_model

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor

class MainViewModel(settingsInteractor: SettingsInteractor): ViewModel() {

    init {
        switchTheme(settingsInteractor.getSwitcherState())
    }

    private fun switchTheme(state: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (state) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    companion object {
        fun factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MainViewModel(settingsInteractor = Creator.provideSettingsInteractor())
                }
            }
    }
}