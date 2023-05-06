package com.practicum.playlistmaker._player.domain.repository

import com.practicum.playlistmaker._player.domain.model.PlayerStates

interface _PlayerRepository {
    
    var state: PlayerStates

    fun prepareMediaPlayer(url: String, listener:() -> Unit)
    fun runTrack()
    fun getTime(): Int
    fun setStopListener(listener: () -> Unit)
    fun stopTrack()
    fun stop()
}
