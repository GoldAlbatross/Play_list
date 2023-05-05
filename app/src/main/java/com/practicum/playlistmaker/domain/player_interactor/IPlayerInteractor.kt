package com.practicum.playlistmaker.domain.player_interactor

import com.practicum.playlistmaker.domain.model.PlayerStates


interface IPlayerInteractor {

    fun prepareMediaPlayer(url: String, listener:() -> Unit)
    fun getState(): PlayerStates
    fun runPlayer()
    fun getTime(): Int
    fun setStopListenerOnMediaPlayer(listener: () -> Unit)
    fun pauseMediaPlayer()
    fun stopMediaPlayer()
}
