package com.practicum.playlistmaker.domain.player.api

import com.practicum.playlistmaker.presentation.activity.player.PlayerState
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
