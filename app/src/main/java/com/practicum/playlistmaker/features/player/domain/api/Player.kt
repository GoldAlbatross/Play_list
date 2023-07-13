package com.practicum.playlistmaker.features.player.domain.api

import com.practicum.playlistmaker.screens.player.model.PlayerState
import kotlinx.coroutines.flow.Flow


interface Player {

    fun prepareMediaPlayer(url: String): Flow<PlayerState>
    fun runTrack()
    fun getTime(): Int
    fun setStopListener(): Flow<PlayerState>
    fun stopTrack()
    fun stop()
    fun getState(): PlayerState
}
