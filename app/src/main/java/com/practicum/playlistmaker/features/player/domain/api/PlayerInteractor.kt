package com.practicum.playlistmaker.features.player.domain.api

import com.practicum.playlistmaker.features.player.domain.model.PlayerStates

interface PlayerInteractor {

    fun prepareMediaPlayer(url: String, listener:() -> Unit)
    fun getState(): PlayerStates
    fun runPlayer()
    fun getTime(): String
    fun setStopListenerOnMediaPlayer(listener: () -> Unit)
    fun pauseMediaPlayer()
    fun stopMediaPlayer()
}