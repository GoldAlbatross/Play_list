package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.application.App
import com.practicum.playlistmaker.player.data.PlayerImpl
import com.practicum.playlistmaker.player.domain.api.Player
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.storage.LocalStorageImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.SearchRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.data.storage.LocalStorage
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
        return ThemeSwitcherImpl(sharedPreferences = App.instance.sharedPreference)
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(player = getPlayer())
    }

    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(repository = getSearchRepository())
    }

    private fun getSearchRepository(): SearchRepository {
        return SearchRepositoryImpl(
            localStorage = getLocalStorage(),
            networkClient = getNetworkClient(),
            context = App.instance
        )
    }
    private fun getPlayer(): Player {
        return PlayerImpl()
    }
    private fun getNetworkClient(): NetworkClient {
        return RetrofitNetworkClient()
    }
    private fun getLocalStorage(): LocalStorage {
        return LocalStorageImpl(
            sharedPreferences = App.instance.sharedPreference,
            gson = App.instance.gson,
        )
    }
}
