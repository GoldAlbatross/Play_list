package com.practicum.playlistmaker._player.domain.interactor

import com.practicum.playlistmaker.models.domain.PlayerStates
import com.practicum.playlistmaker._player.domain.repository._PlayerRepository

class PlayerInteractor(
    private val mediaPlayer: _PlayerRepository
): _PlayerInteractor {

    override fun prepareMediaPlayer(url: String, listener: () -> Unit) {
        mediaPlayer.prepareMediaPlayer(url, listener)
    }

    override fun getState(): PlayerStates {
        return mediaPlayer.state
    }

    override fun runPlayer() {
        mediaPlayer.runTrack()
    }

    override fun getTime(): Int {
        return mediaPlayer.getTime()
    }

    override fun setStopListenerOnMediaPlayer(listener: () -> Unit) {
        mediaPlayer.setStopListener(listener)
    }

    override fun pauseMediaPlayer() {
        mediaPlayer.stopTrack()
    }

    override fun stopMediaPlayer() {
        mediaPlayer.stop()
    }

}