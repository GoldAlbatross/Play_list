package com.practicum.playlistmaker.features.player.domain.api

import com.practicum.playlistmaker.screens.player.model.PlayButtonState


interface Player {

    fun prepareMediaPlayer(url: String, listener:() -> Unit)
    fun runTrack()
    fun getTime(): Int
    fun setStopListener(listener: () -> Unit)
    fun stopTrack()
    fun stop()
    fun getState(): PlayButtonState
}
