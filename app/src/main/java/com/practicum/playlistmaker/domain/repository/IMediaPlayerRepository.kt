package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.model.PlayerStates

interface IMediaPlayerRepository {
    
    var state: PlayerStates

    fun prepareMediaPlayer(url: String, listener:() -> Unit)
    fun runTrack()
    fun getTime(): Int
    fun setStopListener(listener: () -> Unit)
    fun stopTrack()
    fun stop()
}
