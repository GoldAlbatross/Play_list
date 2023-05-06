package com.practicum.playlistmaker._player.domain.interactor

import com.practicum.playlistmaker._player.domain.model.PlayerStates


interface _PlayerInteractor {

    fun prepareMediaPlayer(url: String, listener:() -> Unit)
    fun getState(): PlayerStates
    fun runPlayer()
    fun getTime(): Int
    fun setStopListenerOnMediaPlayer(listener: () -> Unit)
    fun pauseMediaPlayer()
    fun stopMediaPlayer()
}
