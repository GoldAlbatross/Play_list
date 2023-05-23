package com.practicum.playlistmaker.features.player.domain.impl

import com.practicum.playlistmaker.features.player.domain.model.PlayerStates
import com.practicum.playlistmaker.features.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.features.player.domain.api.Player
import com.practicum.playlistmaker.utils.getTimeFormat

class PlayerInteractorImpl(
    private val player: Player
): PlayerInteractor {

    override fun prepareMediaPlayer(url: String, listener: () -> Unit) {
        player.prepareMediaPlayer(url, listener)
    }

    override fun getState(): PlayerStates {
        return player.getState()
    }

    override fun runPlayer() {
        player.runTrack()
    }

    override fun getTime(): String {
        return player.getTime().getTimeFormat()
    }

    override fun setStopListenerOnMediaPlayer(listener: () -> Unit) {
        player.setStopListener(listener)
    }

    override fun pauseMediaPlayer() {
        player.stopTrack()
    }

    override fun stopMediaPlayer() {
        player.stop()
    }

}