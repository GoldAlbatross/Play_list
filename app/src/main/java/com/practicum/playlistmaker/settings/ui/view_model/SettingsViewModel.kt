package com.practicum.playlistmaker.settings.ui.view_model

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor

class SettingsViewModel(private val settingsInteractor: SettingsInteractor): ViewModel() {

    private val themeSwitcherState = SingleLiveEvent<Boolean>()
    init {
        themeSwitcherState.value = settingsInteractor.getSwitcherState()
    }
    fun themeSwitcherState(): LiveData<Boolean> = themeSwitcherState

    fun changeTheme(night: Boolean) {
        if (night == themeSwitcherState.value) return
        settingsInteractor.saveSwitcherState(night)
        themeSwitcherState.postValue(night)
        switchTheme(night)
    }
    private fun switchTheme(night: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (night) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    companion object {
        fun factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SettingsViewModel(settingsInteractor = Creator.provideSettingsInteractor())
                }
            }
    }
}