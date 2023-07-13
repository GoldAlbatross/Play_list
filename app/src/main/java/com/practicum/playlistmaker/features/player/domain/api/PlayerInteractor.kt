package com.practicum.playlistmaker.features.player.domain.api

import com.practicum.playlistmaker.screens.player.model.PlayerState
import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {

    fun prepareMediaPlayer(url: String): Flow<PlayerState>
    fun getState(): PlayerState
    fun runPlayer()
    fun getTime(): String
    fun setStopListenerOnMediaPlayer(): Flow<PlayerState>
    fun pauseMediaPlayer()
    fun stopMediaPlayer()
}
