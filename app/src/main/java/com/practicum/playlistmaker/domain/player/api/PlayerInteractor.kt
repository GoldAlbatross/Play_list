package com.practicum.playlistmaker.domain.player.api

import com.practicum.playlistmaker.presentation.activity.player.PlayerState
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
