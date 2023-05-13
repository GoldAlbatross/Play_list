package com.practicum.playlistmaker.creator

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.main.ui.router.MainRouter
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.player.ui.PlayerPresenter
import com.practicum.playlistmaker.player.ui.PlayerRouter
import com.practicum.playlistmaker.player.ui.PlayerView
import com.practicum.playlistmaker.player.data.PlayerRepositoryImpl
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.ui.router.SettingsRouter

object Creator {

    fun provideMainRouter(context: Context): MainRouter {
        return MainRouter(context)
    }
    fun provideSettingsRouter(context: Context): SettingsRouter {
        return SettingsRouter(context)
    }
    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(repository = getSettingsRepository())
    }
    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl()
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