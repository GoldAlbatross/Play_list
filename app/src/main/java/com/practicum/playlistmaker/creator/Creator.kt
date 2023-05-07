package com.practicum.playlistmaker.creator

import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.player.domain.interactor.PlayerInteractorImpl
import com.practicum.playlistmaker.player.presentation.PlayerPresenter
import com.practicum.playlistmaker.player.presentation.PlayerRouter
import com.practicum.playlistmaker.player.presentation.PlayerView
import com.practicum.playlistmaker.player.repository.PlayerRepositoryImpl

object Creator {

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