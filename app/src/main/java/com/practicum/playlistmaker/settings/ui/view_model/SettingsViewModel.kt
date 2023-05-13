package com.practicum.playlistmaker.settings.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.application.App
import com.practicum.playlistmaker.creator.Creator

class SettingsViewModel(application: Application): AndroidViewModel(application) {

    private val router = Creator.provideSettingsRouter(getApplication<Application>())
    private val settingsInteractor = Creator.provideSettingsInteractor()

//    private val themeSwitcherState = MutableLiveData<ThemeSwitcherState>()
//    fun getThemeSwitcherState(): LiveData<ThemeSwitcherState> = themeSwitcherState

    fun onClickedSharing() { router.sharingPlayListMaker() }
    fun onClickedSupport() { router.getSupport() }
    fun onClickedAgreement() { router.readAgreement() }


    companion object {
        fun factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App)
                    SettingsViewModel(application = app)
                }
            }
    }
}