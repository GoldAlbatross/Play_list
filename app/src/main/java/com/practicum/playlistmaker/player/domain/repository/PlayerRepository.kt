package com.practicum.playlistmaker.player.domain.repository

import com.practicum.playlistmaker.models.domain.PlayerStates

interface PlayerRepository {
    
    var state: PlayerStates

    fun prepareMediaPlayer(url: String, listener:() -> Unit)
    fun runTrack()
    fun getTime(): Int
    fun setStopListener(listener: () -> Unit)
    fun stopTrack()
    fun stop()
}
