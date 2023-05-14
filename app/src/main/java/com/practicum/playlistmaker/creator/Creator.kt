package com.practicum.playlistmaker.creator

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.application.App
import com.practicum.playlistmaker.player.data.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.player.ui.PlayerPresenter
import com.practicum.playlistmaker.player.ui.PlayerRouter
import com.practicum.playlistmaker.player.ui.PlayerView
import com.practicum.playlistmaker.search.data.SearchRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.iml.SearchInteractorImpl
import com.practicum.playlistmaker.settings.data.ThemeSwitcherImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.api.ThemeSwitcher
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl

object Creator {

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(repository = getSettingsRepository())
    }

    private fun getSettingsRepository(): ThemeSwitcher {
        return ThemeSwitcherImpl(sharedPreferences = getSharedPreferences())
    }

    private fun getSharedPreferences(): SharedPreferences {
        return App.instance.sharedPreference
    }

    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(searchRepository = getSearchRepository())
    }

    private fun getSearchRepository(): SearchRepository {
        return SearchRepositoryImpl(sharedPreferences = getSharedPreferences())
    }





    fun createPresenter(
        view: PlayerView,
        activity: AppCompatActivity
    ): PlayerPresenter {
        return PlayerPresenter(
            view = view,
            interactor = PlayerInteractorImpl(PlayerRepositoryImpl()),
            router = PlayerRouter(activity = activity),
        )
    }



}
