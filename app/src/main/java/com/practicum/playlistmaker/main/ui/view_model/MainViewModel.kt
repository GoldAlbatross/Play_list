package com.practicum.playlistmaker.main.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.application.App
import com.practicum.playlistmaker.creator.Creator

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val router = Creator.provideMainRouter(context = getApplication<Application>())

    fun onClickedSearch() { router.openSearchActivity() }
    fun onClickedMediaLib() { router.openMediaLibActivity() }
    fun onClickedSettings() { router.openSettingsActivity() }

    companion object {
        fun factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App)
                    MainViewModel(application = app)
                }
            }
    }
}