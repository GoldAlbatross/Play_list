package com.practicum.playlistmaker.screens.settings.view_model

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.features.storage.domain.api.StorageInteractor
import com.practicum.playlistmaker.utils.DARK_THEME_KEY
import com.practicum.playlistmaker.utils.SingleLiveEvent

class SettingsViewModel(
    private val storageInteractor: StorageInteractor,
): ViewModel() {

    private val themeSwitcherState = SingleLiveEvent<Boolean>()
    init {
        themeSwitcherState.value = storageInteractor.getSwitcherState(DARK_THEME_KEY)
    }
    fun themeSwitcherState(): LiveData<Boolean> = themeSwitcherState

    fun changeTheme(state: Boolean) {
        if (state == themeSwitcherState.value) return
        storageInteractor.saveSwitcherState(key = DARK_THEME_KEY, themeState = state)
        themeSwitcherState.postValue(state)
        switchTheme(themeState = state)
    }
    private fun switchTheme(themeState: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (themeState) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}