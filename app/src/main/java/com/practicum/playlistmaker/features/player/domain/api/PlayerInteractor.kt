package com.practicum.playlistmaker.features.player.domain.api

import com.practicum.playlistmaker.screens.player.model.PlayButtonState

interface PlayerInteractor {

    fun prepareMediaPlayer(url: String, listener:() -> Unit)
    fun getState(): PlayButtonState
    fun runPlayer()
    fun getTime(): String
    fun setStopListenerOnMediaPlayer(listener: () -> Unit)
    fun pauseMediaPlayer()
    fun stopMediaPlayer()
}
