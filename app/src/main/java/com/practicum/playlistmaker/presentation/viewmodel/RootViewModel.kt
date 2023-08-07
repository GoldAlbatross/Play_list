package com.practicum.playlistmaker.presentation.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.shared_prefs.api.StorageInteractor
import com.practicum.playlistmaker.utils.DARK_THEME_KEY

class RootViewModel(
    private val storageInteractor: StorageInteractor
): ViewModel() {


    private fun switchTheme(state: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (state) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun setTheme() {
        val state = storageInteractor.getSwitcherState(key = DARK_THEME_KEY)
        switchTheme(state = state)
    }
}