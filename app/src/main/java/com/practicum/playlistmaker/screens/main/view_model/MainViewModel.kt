package com.practicum.playlistmaker.screens.main.view_model

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.features.shared_preferences.domain.api.LocalStorageInteractor
import com.practicum.playlistmaker.utils.DARK_THEME_KEY

class MainViewModel(
    private val localStorageInteractor: LocalStorageInteractor
): ViewModel() {


    private fun switchTheme(state: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (state) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun setTheme() {
        val state = localStorageInteractor.getSwitcherState(key = DARK_THEME_KEY)
        switchTheme(state = state)
    }
}