package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.player.domain.model.PlayerStates

interface PlayerInteractor {

    fun prepareMediaPlayer(url: String, listener:() -> Unit)
    fun getState(): PlayerStates
    fun runPlayer()
    fun getTime(): Int
    fun setStopListenerOnMediaPlayer(listener: () -> Unit)
    fun pauseMediaPlayer()
    fun stopMediaPlayer()
}
