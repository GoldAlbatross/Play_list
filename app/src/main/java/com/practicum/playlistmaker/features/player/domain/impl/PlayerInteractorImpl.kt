package com.practicum.playlistmaker.features.player.domain.impl

import com.practicum.playlistmaker.features.player.domain.api.Player
import com.practicum.playlistmaker.features.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.screens.player.model.PlayerState
import com.practicum.playlistmaker.utils.getTimeFormat
import kotlinx.coroutines.flow.Flow

class PlayerInteractorImpl(
    private val player: Player
): PlayerInteractor {

    override fun prepareMediaPlayer(url: String): Flow<PlayerState> {
        return player.prepareMediaPlayer(url)
    }

    override fun getState(): PlayerState {
        return player.getState()
    }

    override fun runPlayer() {
        player.runTrack()
    }

    override fun getTime(): String {
        return player.getTime().getTimeFormat()
    }

    override fun setStopListenerOnMediaPlayer(): Flow<PlayerState> {
        return player.setStopListener()
    }

    override fun pauseMediaPlayer() {
        player.stopTrack()
    }

    override fun stopMediaPlayer() {
        player.stop()
    }

}