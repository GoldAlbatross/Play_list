package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.player.domain.model.PlayerStates

interface Player {
    
    var state: PlayerStates

    fun prepareMediaPlayer(url: String, listener:() -> Unit)
    fun runTrack()
    fun getTime(): Int
    fun setStopListener(listener: () -> Unit)
    fun stopTrack()
    fun stop()
}